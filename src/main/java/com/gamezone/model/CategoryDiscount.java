
package com.gamezone.model;
import java.time.LocalDate;

public class CategoryDiscount extends Promotion {
    
    private double discountPercentage; 
    private String categoryObjective;

    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage, String categoryObjective) {
        super(id, name, startDate, endDate);
        this.discountPercentage = discountPercentage;
        this.categoryObjective = categoryObjective;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getCategoryObjective() {
        return categoryObjective;
    }

    public void setCategoryObjective(String categoryObjective) {
        this.categoryObjective = categoryObjective;
    }
    
    @Override
    public double calculateDiscount(Sale sale){
        
      double categoryTotal = 0; 
      
      for(Product product : sale.getProducts()){
          if(categoryObjective.equals("VIDEOGAME") && product instanceof VideoGame){
              categoryTotal += product.getPrice(); 
          }
          
          if(categoryObjective.equals("CONSOLE") && product instanceof Console){
              categoryTotal += product.getPrice(); 
          }
      }
      
      return categoryTotal * discountPercentage / 100;
    }
}
