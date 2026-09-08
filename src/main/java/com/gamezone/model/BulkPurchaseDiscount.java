/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gamezone.model;

/**
 *
 * @author USUARIO
 */
public class BulkPurchaseDiscount extends Promotion {
    
    private int minimumQuantity; 
    private double discountPercentage; 

    public BulkPurchaseDiscount(String id, String name, String startDate, String endDate, int minimumQuantity, double discountPercentage) {
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
     
        return discountPercentage; 
    }
    
}
