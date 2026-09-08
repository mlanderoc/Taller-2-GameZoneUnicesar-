
package com.gamezone.model;
import java.time.LocalDate;
import java.util.List;

public class Sale {
    private String id;
    private LocalDate date;
    private Customer customer;
    private Seller seller;
    private List<Product> products;
    private double totalAmount;
    private String appliedPromotionName;
    private double discountAmount;
    private double subtotal;
    
    /**
     * Creates a new Sale.
     *
     * @param id       unique identifier of the sale
     * @param date     date the sale was made
     * @param customer the customer who made the purchase
     * @param seller   the seller who attended the sale
     * @param products the products included in the sale (must contain at least one)
     * @throws IllegalArgumentException if products is null or empty
     */

    public Sale(String id, LocalDate date, Customer customer, Seller seller, List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("A sale must contain at least one product.");
        }
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.seller = seller;
        this.products = products;
        this.totalAmount = calculateTotal();
        this.discountAmount = 0.0;
        this.appliedPromotionName = null; 
    }
    // Getters and setters

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Seller getSeller() {
        return seller;
    }
    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
       if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("A sale must contain at least one product.");
        }
        this.products = products;
        this.subtotal = calculateTotal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getAppliedPromotionName() {
        return appliedPromotionName;
    }

    public void setAppliedPromotionName(String appliedPromotionName) {
        this.appliedPromotionName = appliedPromotionName;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getSubtotal() {
        return subtotal;
    }
    
    
   
    /**
     * Generates a simple text receipt for the sale.
     *
     * @return the receipt text
     */
    public String generateReceipt() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("Sale ID: ").append(id).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("Customer: ").append(customer.getFullName()).append("\n");
        sb.append("Seller: ").append(seller.getFullName()).append("\n");
        sb.append("Products:\n");
        for (Product product : products) {
            sb.append(" - ").append(product.getDescription()).append("\n");
        }
        sb.append("Subtotal: ").append(subtotal).append("\n");
        
        if (appliedPromotionName != null) {
            sb.append("Descuento aplicado (").append(appliedPromotionName).append("): ")
              .append(discountAmount).append("\n");
        } else {
            sb.append("Descuento aplicado: Ninguno\n");
        }
        
        sb.append("Total: ").append(getTotalAmount());
        return sb.toString();
    }
    /**
     * Calculates the total amount of the sale by summing the price of all products.
     *
     * @return the total amount
     */
    public double calculateTotal() {
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }
    
}
