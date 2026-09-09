
package com.gamezone.model;
import java.time.LocalDate;

/**
 * Represents a promotion that applies a percentage discount
 * to the subtotal of a sale.
 */
public class PercentageDiscount extends Promotion {
    
    private double discountPercentage;
    
    /**
     * Creates a percentage-based promotion.
     *
     * @param id the promotion identifier
     * @param name the promotion name
     * @param startDate the date when the promotion becomes active
     * @param endDate the date when the promotion expires
     * @param discountPercentage the percentage discount to apply
     */
    public PercentageDiscount(String id, String name, LocalDate startDate, LocalDate      endDate, double discountPercentage ) {
        super(id, name, startDate, endDate);
        this.discountPercentage = discountPercentage;
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
     * Calculates the monetary discount by applying the configured percentage
     * to the subtotal of the given sale.
     *
     * @param sale the sale used to calculate the discount
     * @return the calculated discount amount
     */
   @Override
   public double calculateDiscount(Sale sale){
       
        return sale.calculateTotal() * discountPercentage / 100;
   }

   
    
}
