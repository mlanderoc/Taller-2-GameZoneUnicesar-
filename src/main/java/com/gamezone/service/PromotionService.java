package com.gamezone.service;

import com.gamezone.persistence.PromotionRepository;
import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionService {

    private final PromotionRepository repository;
    private final List<Promotion> promotions;

    public PromotionService(PromotionRepository repository) {
        this.repository = repository;
        this.promotions = repository.loadAll();
    }

    public Promotion findById(String id) {
        for (Promotion promotion : promotions) {
            if (promotion.getId().equals(id)) {
                return promotion;
            }
        }

        return null;
    }

    public void registerPercentageDiscount(String id, String name, String startDate, String endDate, double discountPercentage) {

        if (findById(id) != null) {
            throw new IllegalArgumentException("A promotion with this ID already exists");
        }
        PercentageDiscount percentageDiscounts = new PercentageDiscount(id, name, LocalDate.MAX, LocalDate.MAX, discountPercentage);
        promotions.add(percentageDiscounts);
        repository.saveAll(promotions);
    }

    public void registerCategoryDiscount(String id, String name, String startDate, String endDate, double discountPercentage, String categoryObjective) {

        if (findById(id) != null) {
            throw new IllegalArgumentException(
                    "A promotion with this ID already exists"
            );
        }
        CategoryDiscount categoryDiscounts = new CategoryDiscount(id, name, LocalDate.MAX, LocalDate.MAX, discountPercentage, categoryObjective);
        promotions.add(categoryDiscounts);
        repository.saveAll(promotions);
    }

    public void registerBulkPurchaseDiscount(String id, String name, String startDate, String endDate, int minimumQuantity, double discountPercentage) {

        if (findById(id) != null) {
            throw new IllegalArgumentException(
                    "A promotion with this ID already exists"
            );
        }

        BulkPurchaseDiscount bulkPurchaseDiscounts = new BulkPurchaseDiscount(id, name, LocalDate.MAX, LocalDate.MAX, minimumQuantity, discountPercentage);
        promotions.add(bulkPurchaseDiscounts);
        repository.saveAll(promotions);
    }

    public List<Promotion> listAllPromotions() {
        return promotions;
    }

    public List<Promotion> listActivePromotions() {
        LocalDate today = LocalDate.now();
        List<Promotion> all = new ArrayList<>();

        List<Promotion> active = new ArrayList<>();
        for (Promotion p : all) {
            if (p.isActive(today)) {
                active.add(p);
            }
        }
        return active;
    }

    public Promotion findBestPromotionFor(Sale sale) {
        List<Promotion> active = listActivePromotions();

        Promotion best = null;
        double bestDiscount = 0.0;

        for (Promotion p : active) {
            double discount = p.calculateDiscount(sale);

            if (discount > bestDiscount) {
                bestDiscount = discount;
                best = p;
            }
        }
        return (best != null && bestDiscount > 0) ? best : null;
    }
}
