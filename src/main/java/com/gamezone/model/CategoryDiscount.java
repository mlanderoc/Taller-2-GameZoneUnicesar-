
package com.gamezone.model;
import java.time.LocalDate;

/**
 * Represents a promotion that applies a percentage discount
 * to products belonging to a specific category.
 */
public class CategoryDiscount extends Promotion {
    
    private double discountPercentage; 
    private String categoryObjective;

    /**
     * Creates a category-based discount promotion.
     *
     * @param id the promotion identifier
     * @param name the promotion name
     * @param startDate the date when the promotion becomes active
     * @param endDate the date when the promotion expires
     * @param discountPercentage the percentage discount to apply
     * @param categoryObjective the target product category
     */
    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage, String categoryObjective) {
        super(id, name, startDate, endDate);
        this.discountPercentage = discountPercentage;
        this.categoryObjective = categoryObjective;
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
     * Returns the product category targeted by this promotion.
     *
     * @return the target product category
     */
    public String getCategoryObjective() {
        return categoryObjective;
    }

    /**
     * Sets the product category targeted by this promotion.
     *
     * @param categoryObjective the new target product category
     */
    public void setCategoryObjective(String categoryObjective) {
        this.categoryObjective = categoryObjective;
    }
    
    /**
     * Calculates the monetary discount by applying the configured percentage
     * only to the products that belong to the target category.
     *
     * @param sale the sale used to calculate the discount
     * @return the calculated discount amount for the target category
     */
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
