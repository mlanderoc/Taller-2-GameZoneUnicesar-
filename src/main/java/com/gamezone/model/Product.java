
package com.gamezone.model;
import java.io.Serializable;

/**
 * Represents a generic product sold by the GameZone store.
 * This abstract class contains the common data shared by all product types.
 */
public abstract class Product implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String title; 
    private double price; 
    private int stock;
    
    /**
     * Creates a product with its common information.
     *
     * @param id the unique identifier of the product
     * @param title the title or name of the product
     * @param price the price of the product
     * @param stock the available quantity in inventory
     */
    public Product(int id, String title, double price, int stock) {
        
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
    }
    
    /**
     * Returns the product identifier.
     *
     * @return the product id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Sets the product identifier.
     *
     * @param id the new product identifier
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Returns the title of the product.
     *
     * @return the product title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title of the product.
     *
     * @param title the new product title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Returns the price of the product.
     *
     * @return the product price
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Sets the price of the product.
     *
     * @param price the new product price
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * Returns the stock of the product.
     *
     * @return the product stock
     */
    public int getStock() {
        return stock;
    }
    
    /**
     * Sets the stock of the product.
     *
     * @param stock the new product stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

     /**
     * Returns a complete description of the product.
     *
     * @return the product description
     */
    public abstract String getDescription(); 
    
    /**
     * Updates the available stock by the specified quantity.
     *
     * @param quantity the quantity to add to or subtract from the stock
     */
    public void updateStock(int quantity){
        
    this.stock += quantity; 
    
    }
    
}
