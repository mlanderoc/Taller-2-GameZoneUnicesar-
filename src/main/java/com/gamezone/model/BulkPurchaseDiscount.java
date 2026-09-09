
package com.gamezone.model;
import java.time.LocalDate;

/**
 * Represents a promotion that applies a percentage discount
 * when a sale includes a minimum number of products.
 */
public class BulkPurchaseDiscount extends Promotion {
    
    private int minimumQuantity; 
    private double discountPercentage; 

    /**
     * Creates a bulk purchase discount promotion.
     *
     * @param id the promotion identifier
     * @param name the promotion name
     * @param startDate the date when the promotion becomes active
     * @param endDate the date when the promotion expires
     * @param minimumQuantity the minimum number of products required
     * @param discountPercentage the percentage discount to apply
     */
    public BulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate, int minimumQuantity, double discountPercentage) {
        super(id, name, startDate, endDate);
        this.minimumQuantity = minimumQuantity;
        this.discountPercentage = discountPercentage;
    }

    /**
     * Returns the minimum number of products required to apply
     * this promotion.
     *
     * @return the minimum required product quantity
     */
    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    /**
     * Sets the minimum number of products required to apply
     * this promotion.
     *
     * @param minimumQuantity the new minimum required quantity
     */
    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    /**
     * Returns the percentage discount configured for this promotion.
     *
     * @return the discount percentage
     */
    public double getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * Sets the percentage discount for this promotion.
     *
     * @param discountPercentage the new discount percentage
     */
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    /**
     * Calculates the monetary discount for a sale when it contains
     * at least the required minimum number of products.
     *
     * @param sale the sale used to calculate the discount
     * @return the calculated discount amount, or 0.0 if the sale
     *         does not meet the minimum product quantity
     */
    @Override
    public double calculateDiscount(Sale sale){
     
        if (sale.getProducts().size() >= minimumQuantity) {
            return sale.calculateTotal() * discountPercentage / 100; 
        }
        return 0; 
    }
    
}
