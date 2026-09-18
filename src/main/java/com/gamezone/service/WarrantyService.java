package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.WarrantyRecord;
import com.gamezone.persistence.WarrantyRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles business rules for assigning, querying, and persisting warranties
 * associated to consoles sold in the system.
 */
public class WarrantyService {

    private WarrantyRepository repository;
    private SaleService saleService;
    private ProductService productService;

    public WarrantyService(WarrantyRepository repository, SaleService saleService, ProductService productService) {
        this.repository = repository;
        this.saleService = saleService;
        this.productService = productService;
    }

    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) {
        BasicWarranty warranty = new BasicWarranty(UUID.randomUUID().toString(), product, sale, startDate);
        save(warranty);
        return warranty;
    }

    public ExtendedWarranty assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) {
        ExtendedWarranty warranty = new ExtendedWarranty(UUID.randomUUID().toString(), product, sale, startDate);
        save(warranty);
        return warranty;
    }

    public Warranty findWarrantyByProduct(String productId, String saleId) {
        for (Warranty warranty : listAllWarranties()) {
            if (warranty.getProduct().getId().equals(productId) && warranty.getSale().getId().equals(saleId)) {
                return warranty;
            }
        }
        return null;
    }

    public List<Warranty> listAllWarranties() {
        List<Warranty> warranties = new ArrayList<>();
        for (WarrantyRecord record : repository.loadAll()) {
            warranties.add(toWarranty(record));
        }
        return warranties;
    }

    public List<Warranty> listActiveWarranties() {
        List<Warranty> active = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Warranty warranty : listAllWarranties()) {
            if (warranty.isActive(today)) {
                active.add(warranty);
            }
        }
        return active;
    }

    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {
        List<Warranty> expiringSoon = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(daysAhead);
        for (Warranty warranty : listAllWarranties()) {
            long daysUntilEnd = ChronoUnit.DAYS.between(today, warranty.getEndDate());
            if (daysUntilEnd >= 0 && !warranty.getEndDate().isAfter(limit)) {
                expiringSoon.add(warranty);
            }
        }
        return expiringSoon;
    }

    private void save(Warranty warranty) {
        List<WarrantyRecord> records = repository.loadAll();
        records.add(toRecord(warranty));
        repository.saveAll(records);
    }

    private WarrantyRecord toRecord(Warranty warranty) {
        return new WarrantyRecord(
                warranty.getWarrantyId(),
                warranty.getProduct().getId(),
                warranty.getSale().getId(),
                warranty.getWarrantyType(),
                warranty.getStartDate(),
                warranty.getEndDate()
        );
    }

    private Warranty toWarranty(WarrantyRecord record) {
        Product product = productService.findById(record.getProductId());
        Sale sale = saleService.findSaleById(record.getSaleId());

        if ("Garantía Extendida".equals(record.getWarrantyType())) {
            return new ExtendedWarranty(record.getWarrantyId(), product, sale, record.getStartDate());
        } else {
            return new BasicWarranty(record.getWarrantyId(), product, sale, record.getStartDate());
        }
    }
}