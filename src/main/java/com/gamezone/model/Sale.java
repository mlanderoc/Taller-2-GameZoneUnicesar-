
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
        this.products = products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String generateReceipt() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("Sale ID: ").append(id).append("\n");
        sb.append("Date: ").append(date).append("\n");
        //sb.append("Customer: ").append(customer.getFullName()).append("\n");
        //sb.append("Seller: ").append(seller.getFullName()).append("\n");
        sb.append("Products:\n");
        for (Product product : products) {
            sb.append(" - ").append(product.getDescription()).append("\n");
        }
        sb.append("Total: ").append(totalAmount);
        return sb.toString();
    }
    
    public double calculateTotal() {
        double total = 0.0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }
    
}
