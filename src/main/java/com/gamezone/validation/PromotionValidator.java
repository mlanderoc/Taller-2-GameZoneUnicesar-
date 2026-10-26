package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.model.Promotion;
import java.time.LocalDate;

/**
 * Provides static validation methods exclusively for promotional campaigns
 * (base attributes, date windows, percentage bounds, target categories, and
 * minimum bulk quantities), satisfying the Single Responsibility Principle (SRP).
 */
public class PromotionValidator {

    private PromotionValidator() {
        // Utility class with static methods only
    }

    /**
     * Validates base promotion attributes and uniqueness before registration.
     *
     * @param id                 promotion identifier
     * @param name               promotion name
     * @param startDate          promotion start date
     * @param endDate            promotion expiration date
     * @param discountPercentage percentage discount (between 1 and 100)
     * @param existingPromotion  existing promotion with the same ID, or null if none
     * @throws InvalidDataException  if any field or range is invalid
     * @throws BusinessRuleException if a promotion with the same ID already exists
     */
    public static void validatePromotionBase(String id, String name, LocalDate startDate, LocalDate endDate,
                                             double discountPercentage, Promotion existingPromotion) {
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
        if (existingPromotion != null) {
            throw new BusinessRuleException("Ya existe una promoción registrada con el ID: " + id);
        }
    }

    /**
     * Validates that the target category of a CategoryDiscount promotion is one of
     * the supported catalog categories ("VIDEOGAME", "CONSOLE", or "ACCESSORY").
     *
     * @param categoryObjective target product category
     * @throws InvalidDataException if the category is null or unsupported
     */
    public static void validateCategoryObjective(String categoryObjective) {
        if (categoryObjective == null
                || (!"VIDEOGAME".equalsIgnoreCase(categoryObjective.trim())
                && !"CONSOLE".equalsIgnoreCase(categoryObjective.trim())
                && !"ACCESSORY".equalsIgnoreCase(categoryObjective.trim()))) {
            throw new InvalidDataException("categoría", "Categoría inválida. Categorías permitidas: VIDEOGAME, CONSOLE, ACCESSORY.");
        }
    }

    /**
     * Validates that the minimum quantity for a BulkPurchaseDiscount promotion is positive.
     *
     * @param minimumQuantity minimum item count required to trigger the bulk discount
     * @throws InvalidDataException if minimumQuantity is not greater than zero
     */
    public static void validateMinimumQuantity(int minimumQuantity) {
        if (minimumQuantity <= 0) {
            throw new InvalidDataException("cantidad mínima", "La cantidad mínima de productos debe ser mayor que cero.");
        }
    }
}
