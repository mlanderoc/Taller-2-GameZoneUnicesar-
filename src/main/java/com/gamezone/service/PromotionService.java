package com.gamezone.service;

import com.gamezone.persistence.PromotionRepository;
import com.gamezone.persistence.Repository;
import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.validation.PromotionValidator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages business operations for promotional campaigns, depending on the
 * {@link Repository} abstraction (DIP) and delegating validation rules to
 * {@link PromotionValidator} (SRP).
 */
public class PromotionService {

    private final Repository<Promotion> repository;
    private final List<Promotion> promotions;

    /**
     * Creates the promotion service using the given repository abstraction.
     *
     * @param repository the repository used to load and save promotions
     */
    public PromotionService(Repository<Promotion> repository) {
        this.repository = repository;
        this.promotions = repository.loadAll();
    }

    /**
     * Creates the promotion service using a concrete {@link PromotionRepository}.
     *
     * @param repository the promotion repository
     */
    public PromotionService(PromotionRepository repository) {
        this((Repository<Promotion>) repository);
    }

    public Promotion findById(String id) {
        for (Promotion promotion : promotions) {
            if (promotion.getId().equals(id)) {
                return promotion;
            }
        }
        return null;
    }

    public void registerPercentageDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage) {
        validatePromotionBase(id, name, startDate, endDate, discountPercentage);
        PercentageDiscount percentageDiscount = new PercentageDiscount(id, name, startDate, endDate, discountPercentage);
        promotions.add(percentageDiscount);
        repository.saveAll(promotions);
    }

    /**
     * Registers a new category discount promotion after validating the target category.
     *
     * @param id the promotion identifier
     * @param name the promotion name
     * @param startDate the start date of the promotion
     * @param endDate the expiration date of the promotion
     * @param discountPercentage the discount percentage to apply
     * @param categoryObjective the target category ("VIDEOGAME", "CONSOLE", or "ACCESSORY")
     */
    public void registerCategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage, String categoryObjective) {
        validatePromotionBase(id, name, startDate, endDate, discountPercentage);
        PromotionValidator.validateCategoryObjective(categoryObjective);
        CategoryDiscount categoryDiscount = new CategoryDiscount(id, name, startDate, endDate, discountPercentage, categoryObjective.trim().toUpperCase());
        promotions.add(categoryDiscount);
        repository.saveAll(promotions);
    }

    public void registerBulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate, int minimumQuantity, double discountPercentage) {
        validatePromotionBase(id, name, startDate, endDate, discountPercentage);
        PromotionValidator.validateMinimumQuantity(minimumQuantity);
        BulkPurchaseDiscount bulkPurchaseDiscount = new BulkPurchaseDiscount(id, name, startDate, endDate, minimumQuantity, discountPercentage);
        promotions.add(bulkPurchaseDiscount);
        repository.saveAll(promotions);
    }

    private void validatePromotionBase(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage) {
        PromotionValidator.validatePromotionBase(id, name, startDate, endDate, discountPercentage, findById(id));
    }

    public List<Promotion> listAllPromotions() {
        return promotions;
    }

    public List<Promotion> listActivePromotions() {
        LocalDate today = LocalDate.now();
        List<Promotion> active = new ArrayList<>();
        for (Promotion p : promotions) {
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
        return best;
    }
}
