package com.gamezone.service;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
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
        if (categoryObjective == null
                || (!"VIDEOGAME".equalsIgnoreCase(categoryObjective.trim())
                && !"CONSOLE".equalsIgnoreCase(categoryObjective.trim())
                && !"ACCESSORY".equalsIgnoreCase(categoryObjective.trim()))) {
            throw new InvalidDataException("categoría", "Categoría inválida. Categorías permitidas: VIDEOGAME, CONSOLE, ACCESSORY.");
        }
        CategoryDiscount categoryDiscount = new CategoryDiscount(id, name, startDate, endDate, discountPercentage, categoryObjective.trim().toUpperCase());
        promotions.add(categoryDiscount);
        repository.saveAll(promotions);
    }

    public void registerBulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate, int minimumQuantity, double discountPercentage) {
        validatePromotionBase(id, name, startDate, endDate, discountPercentage);
        if (minimumQuantity <= 0) {
            throw new InvalidDataException("cantidad mínima", "La cantidad mínima de productos debe ser mayor que cero.");
        }
        BulkPurchaseDiscount bulkPurchaseDiscount = new BulkPurchaseDiscount(id, name, startDate, endDate, minimumQuantity, discountPercentage);
        promotions.add(bulkPurchaseDiscount);
        repository.saveAll(promotions);
    }

    private void validatePromotionBase(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException("id", "El identificador de la promoción no puede estar vacío.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("nombre", "El nombre de la promoción no puede estar vacío.");
        }
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new InvalidDataException("fechas", "La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        if (discountPercentage <= 0 || discountPercentage > 100) {
            throw new InvalidDataException("porcentaje", "El porcentaje de descuento debe estar entre 1 y 100.");
        }
        if (findById(id) != null) {
            throw new BusinessRuleException("Ya existe una promoción registrada con el ID: " + id);
        }
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