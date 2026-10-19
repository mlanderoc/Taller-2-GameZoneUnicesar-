package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.SaleRecord;
import com.gamezone.persistence.SaleRepository;
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

    private final WarrantyRepository repository;
    private final SaleRepository saleRepository;
    private final ProductService productService;

    /**
     * Creates the warranty service resolving sale and product references from
     * their identifiers without depending on SaleService.
     *
     * @param repository the warranty repository
     * @param saleRepository the sale repository used to resolve sale identifiers
     * @param productService the product service used to resolve product identifiers
     */
    public WarrantyService(WarrantyRepository repository, SaleRepository saleRepository, ProductService productService) {
        this.repository = repository;
        this.saleRepository = saleRepository;
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
            if (warranty.getProduct() != null && warranty.getSale() != null
                    && warranty.getProduct().getId().equals(productId)
                    && warranty.getSale().getId().equals(saleId)) {
                return warranty;
            }
        }
        return null;
    }

    public List<Warranty> listAllWarranties() {
        List<Warranty> warranties = new ArrayList<>();
        for (WarrantyRecord record : repository.loadAll()) {
            Warranty warranty = toWarranty(record);
            if (warranty != null) {
                warranties.add(warranty);
            }
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

    /**
     * Cancels and removes from persistence all warranties (basic and/or extended)
     * associated with the given product and sale when a console is returned.
     *
     * @param productId identifier of the returned console
     * @param saleId    identifier of the original sale
     * @return refundable cost of cancelled warranties (0.0 if only basic, or extended warranty cost)
     */
    public double cancelWarranties(String productId, String saleId) {
        List<WarrantyRecord> allRecords = repository.loadAll();
        List<WarrantyRecord> remainingRecords = new ArrayList<>();
        double refundableCost = 0.0;

        for (WarrantyRecord record : allRecords) {
            if (record.getProductId().equals(productId) && record.getSaleId().equals(saleId)) {
                Warranty warranty = toWarranty(record);
                if (warranty != null) {
                    refundableCost += warranty.getAdditionalCost();
                }
            } else {
                remainingRecords.add(record);
            }
        }

        if (remainingRecords.size() != allRecords.size()) {
            repository.saveAll(remainingRecords);
        }

        return refundableCost;
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
        Sale sale = resolveSaleById(record.getSaleId(), product);
        if (product == null || sale == null) {
            return null;
        }

        if ("Garantía Extendida".equals(record.getWarrantyType())) {
            return new ExtendedWarranty(record.getWarrantyId(), product, sale, record.getStartDate());
        } else {
            return new BasicWarranty(record.getWarrantyId(), product, sale, record.getStartDate());
        }
    }

    private Sale resolveSaleById(String saleId, Product fallbackProduct) {
        for (SaleRecord saleRecord : saleRepository.loadAll()) {
            if (saleRecord.getSaleId().equals(saleId)) {
                List<Product> products = new ArrayList<>();
                for (String pid : saleRecord.getProductIds()) {
                    Product item = productService.findById(pid);
                    if (item != null) {
                        products.add(item);
                    }
                }
                if (products.isEmpty() && fallbackProduct != null) {
                    products.add(fallbackProduct);
                }
                if (products.isEmpty()) {
                    return null;
                }
                Sale sale = new Sale(saleRecord.getSaleId(), saleRecord.getDate(), null, null, products);
                sale.setSubtotal(saleRecord.getSubtotal());
                sale.setAppliedPromotionName(saleRecord.getAppliedPromotionName());
                sale.setDiscountAmount(saleRecord.getDiscountAmount());
                return sale;
            }
        }
        return null;
    }
}