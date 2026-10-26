package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import java.time.LocalDate;

/**
 * Provides static validation methods for product and accessory data,
 * existence verification, duplicate identifier checks, and stock availability.
 */
public class ProductValidator {

    private ProductValidator() {
        // Utility class with static methods only
    }

    /**
     * Validates that basic product data meets format and range requirements:
     * non-null and non-empty identifier and title, positive price, and non-negative stock.
     *
     * @param id    product identifier
     * @param title product title
     * @param price product unit price (must be greater than zero)
     * @param stock initial inventory quantity (must be zero or greater)
     * @throws InvalidDataException if any field fails validation
     */
    public static void validateProductData(String id, String title, double price, int stock) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException("id", "El identificador del producto no puede estar vacío.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidDataException("title", "El título del producto no puede estar vacío.");
        }
        if (price <= 0) {
            throw new InvalidDataException("price", "El precio debe ser un valor positivo mayor que cero.");
        }
        if (stock < 0) {
            throw new InvalidDataException("stock", "El inventario inicial no puede ser negativo.");
        }
    }

    /**
     * Validates that the given product has enough stock to fulfill the requested quantity.
     *
     * @param product           product to verify
     * @param requestedQuantity quantity required for the operation
     * @throws ResourceNotFoundException if the product reference is null
     * @throws BusinessRuleException     if available stock is less than the requested quantity
     */
    public static void validateStockAvailability(Product product, int requestedQuantity) {
        validateProductExists(product, "");
        if (product.getStock() < requestedQuantity) {
            throw new BusinessRuleException(
                    "Stock insuficiente para el producto '" + product.getTitle()
                    + "' (disponible: " + product.getStock() + ", solicitado: " + requestedQuantity + ").");
        }
    }

    /**
     * Validates that a looked-up product reference is not null.
     *
     * @param product   product reference returned from a lookup
     * @param productId identifier that was searched
     * @throws ResourceNotFoundException if the product is null
     */
    public static void validateProductExists(Product product, String productId) {
        if (product == null) {
            throw new ResourceNotFoundException("producto", productId);
        }
    }

    /**
     * Validates that no existing product already uses the given identifier.
     *
     * @param existingProduct product found with the same identifier, or null if none
     * @param newId           identifier being registered
     * @throws BusinessRuleException if a product with the same identifier already exists
     */
    public static void validateUniqueProduct(Product existingProduct, String newId) {
        if (existingProduct != null) {
            throw new BusinessRuleException("Ya existe un producto o accesorio registrado con el ID: " + newId);
        }
    }

    /**
     * Validates cable-specific attributes (positive length in meters).
     *
     * @param lengthInMeters cable length in meters
     * @throws InvalidDataException if lengthInMeters is not positive
     */
    public static void validateCableData(double lengthInMeters) {
        if (lengthInMeters <= 0) {
            throw new InvalidDataException("longitud", "La longitud del cable debe ser mayor que cero.");
        }
    }

    /**
     * Validates memory-specific attributes (positive storage capacity in GB).
     *
     * @param storageCapacityGb storage capacity in gigabytes
     * @throws InvalidDataException if storageCapacityGb is not positive
     */
    public static void validateMemoryData(int storageCapacityGb) {
        if (storageCapacityGb <= 0) {
            throw new InvalidDataException("capacidad", "La capacidad de almacenamiento debe ser mayor que cero.");
        }
    }

    /**
     * Delegates base promotion validation to {@link PromotionValidator} for backward compatibility.
     *
     * @param id                 promotion identifier
     * @param name               promotion name
     * @param startDate          promotion start date
     * @param endDate            promotion expiration date
     * @param discountPercentage percentage discount (between 1 and 100)
     * @param existingPromotion  existing promotion with the same ID, or null if none
     */
    public static void validatePromotionBase(String id, String name, LocalDate startDate, LocalDate endDate,
                                             double discountPercentage, Promotion existingPromotion) {
        PromotionValidator.validatePromotionBase(id, name, startDate, endDate, discountPercentage, existingPromotion);
    }

    /**
     * Delegates category objective validation to {@link PromotionValidator} for backward compatibility.
     *
     * @param categoryObjective target product category
     */
    public static void validateCategoryObjective(String categoryObjective) {
        PromotionValidator.validateCategoryObjective(categoryObjective);
    }

    /**
     * Delegates minimum quantity validation to {@link PromotionValidator} for backward compatibility.
     *
     * @param minimumQuantity minimum item count required to trigger the bulk discount
     */
    public static void validateMinimumQuantity(int minimumQuantity) {
        PromotionValidator.validateMinimumQuantity(minimumQuantity);
    }
}
