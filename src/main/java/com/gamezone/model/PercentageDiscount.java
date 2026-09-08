
package com.gamezone.model;
import java.time.LocalDate;

public class PercentageDiscount extends Promotion {
    
    private double discountPercentage;

    public PercentageDiscount(String id, String name, LocalDate startDate, LocalDate      endDate, double discountPercentage ) {
        super(id, name, startDate, endDate);
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    
   @Override
   public double calculateDiscount(Sale sale){
       
        return sale.calculateTotal() * discountPercentage / 100;
   }

   
    
}
