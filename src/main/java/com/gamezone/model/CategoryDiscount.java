
package com.gamezone.model;


public class CategoryDiscount extends Promotion {
    
    private double discountPercentage; 
    private String categoryObjective;

    public CategoryDiscount(String id, String name, String startDate, String endDate, double discountPercentage, String categoryObjective) {
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
        
        return discountPercentage; 
    }
}
