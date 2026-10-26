package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.Repository;
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
 * associated to consoles sold in the system, adhering to SRP, OCP, ISP, and DIP.
 */
public class WarrantyService {

    private final Repository<WarrantyRecord> repository;
    private final ProductCatalogLookup productService;
    private final SaleRecordResolver saleRecordResolver;

    /**
     * Creates the warranty service using repository and catalog lookup abstractions
     * (DIP & ISP).
     *
     * @param repository     the warranty record repository abstraction
     * @param saleRepository the sale record repository abstraction
     * @param productLookup  the read-only product catalog lookup
     */
    public WarrantyService(Repository<WarrantyRecord> repository,
                           Repository<SaleRecord> saleRepository,
                           ProductCatalogLookup productLookup) {
        this.repository = repository;
        this.productService = productLookup;
        this.saleRecordResolver = new SaleRecordResolver(saleRepository, productLookup);
    }

    /**
     * Creates the warranty service resolving sale and product references from
     * their identifiers without depending on SaleService.
     *
     * @param repository     the warranty repository
     * @param saleRepository the sale repository used to resolve sale identifiers
     * @param productService the product service used to resolve product identifiers
     */
    public WarrantyService(WarrantyRepository repository, SaleRepository saleRepository, ProductService productService) {
        this((Repository<WarrantyRecord>) repository, (Repository<SaleRecord>) saleRepository, (ProductCatalogLookup) productService);
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
        Sale sale = saleRecordResolver.resolveSaleById(record.getSaleId(), product);
        if (product == null || sale == null) {
            return null;
        }
        return Warranty.createFromType(record.getWarrantyType(), record.getWarrantyId(), product, sale, record.getStartDate());
    }
}
