package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.WarrantyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WarrantyService {

    private WarrantyRepository Repository;

    public WarrantyService(WarrantyRepository Repository) {
        this.Repository = Repository;
    }

    

    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) {

        if (product == null || sale == null || startDate == null) {
            throw new IllegalArgumentException("Product, sale and start date cannot be null.");
        }

        List<Warranty> warranties = Repository.loadAll();

        BasicWarranty warranty = new BasicWarranty(
                "W-" + (warranties.size() + 1),
                product,
                sale,
                startDate
        );

        warranties.add(warranty);
        Repository.saveAll(warranties);

        return warranty;
    }

    public ExtendedWarranty assignExtendedWarranty(Product product,Sale sale, LocalDate startDate) {

        if (product == null || sale == null || startDate == null) {
            throw new IllegalArgumentException("Product, sale and start date cannot be null.");
        }

        List<Warranty> warranties = Repository.loadAll();

        ExtendedWarranty warranty = new ExtendedWarranty("W-" + (warranties.size() + 1),product,sale,startDate);

        warranties.add(warranty);
        Repository.saveAll(warranties);

        return warranty;
    }

    public Warranty findWarrantyByProduct(String productId, String saleId) {

        if (productId == null || saleId == null) {
            throw new IllegalArgumentException("Product ID and sale ID cannot be null.");
        }

        List<Warranty> warranties = Repository.loadAll();

        for (Warranty warranty : warranties) {
            if (warranty.getProduct().getId().equals(productId)
                    && warranty.getSale().getId().equals(saleId)) {
                return warranty;
            }
        }

        return null;
    }

    public List<Warranty> listAllWarranties() {
        return Repository.loadAll();
    }

    public List<Warranty> listActiveWarranties() {

        List<Warranty> activeWarranties = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Warranty warranty : Repository.loadAll()) {
            if (warranty.isActive(today)) {
                activeWarranties.add(warranty);
            }
        }

        return activeWarranties;
    }

    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {

        if (daysAhead < 0) {
            throw new IllegalArgumentException("Days ahead cannot be negative.");
        }

        List<Warranty> expiringWarranties = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate limitDate = today.plusDays(daysAhead);

        for (Warranty warranty : Repository.loadAll()) {

            LocalDate endDate = warranty.getEndDate();

            if (!endDate.isBefore(today) && !endDate.isAfter(limitDate)) {
                expiringWarranties.add(warranty);
            }
        }

        return expiringWarranties;
    }
}