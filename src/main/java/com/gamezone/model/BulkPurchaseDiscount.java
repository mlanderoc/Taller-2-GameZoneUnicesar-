
package com.gamezone.model;
import java.time.LocalDate;

public class BulkPurchaseDiscount extends Promotion {
    
    private int minimumQuantity; 
    private double discountPercentage; 

    public BulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate, int minimumQuantity, double discountPercentage) {
        super(id, name, startDate, endDate);
        this.minimumQuantity = minimumQuantity;
        this.discountPercentage = discountPercentage;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    @Override
    public double calculateDiscount(Sale sale){
     
        if (sale.getProducts().size() >= minimumQuantity) {
            return sale.calculateTotal() * discountPercentage / 100; 
        }
        return 0; 
    }
    
}
