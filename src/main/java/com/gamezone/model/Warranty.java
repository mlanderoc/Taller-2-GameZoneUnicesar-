
package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a general warranty associated with a product and a sale.
 * It defines common warranty information and behavior.
 */
public abstract class Warranty {
    
    private String id;
    private Product product;
    private Sale sale;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a warranty and calculates its expiration date according to the
     * duration defined by the concrete warranty type.
     *
     * @param id unique identifier of the warranty
     * @param product product covered by the warranty
     * @param sale sale associated with the warranty
     * @param startDate start date of the warranty
     */
    public Warranty(String id, Product product, Sale sale, LocalDate startDate) {
        this.id = id;
        this.product = product;
        this.sale = sale;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(getDurationInMonths());
    }

    /**
     * Returns the unique identifier of the warranty.
     *
     * @return warranty identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the product covered by this warranty.
     *
     * @return covered product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Returns the sale associated with this warranty.
     *
     * @return associated sale
     */
    public Sale getSale() {
        return sale;
    }

    /**
     * Returns the start date of the warranty.
     *
     * @return warranty start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the expiration date of the warranty.
     *
     * @return warranty expiration date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns the warranty duration in months.
     *
     * @return warranty duration in months
     */
    public abstract int getDurationInMonths();

    /**
     * Returns the type name of the warranty.
     *
     * @return warranty type name
     */
    public abstract String getWarrantyType();

    /**
     * Returns the additional cost added by this warranty.
     *
     * @return additional warranty cost
     */
    public abstract double getAdditionalCost();

    /**
     * Checks whether the warranty is active on a specific date.
     *
     * @param date date to check
     * @return true if the warranty is active; otherwise false
     */
    public boolean isActive(LocalDate date) {

        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    
    /**
     * Generates a formatted certificate with the warranty information.
     *
     * @return warranty certificate text
     */
    public String generateWarrantyCertificate() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID de garantía: ").append(id).append("\n");
        sb.append("Tipo de garantía: ").append(getWarrantyType()).append("\n");
        sb.append("Producto: ").append(product.getTitle()).append("\n");
        sb.append("ID de venta: ").append(sale.getId()).append("\n");
        sb.append("Fecha de inicio: ").append(startDate).append("\n");
        sb.append("Fecha de vencimiento: ").append(endDate).append("\n");
        sb.append("Costo adicional: ").append(getAdditionalCost());

        return sb.toString();
    }
    
}
