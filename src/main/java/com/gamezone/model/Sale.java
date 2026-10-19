
package com.gamezone.model;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
    private double extendedWarrantyCost;
    
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
        this.subtotal = calculateTotal();
        this.discountAmount = 0.0;
        this.extendedWarrantyCost = 0.0;
        this.appliedPromotionName = null;
        this.totalAmount = getTotalAmount();
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
        this.totalAmount = getTotalAmount();
    }

    public double getTotalAmount() {
        return subtotal - discountAmount + extendedWarrantyCost;
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
        this.totalAmount = getTotalAmount();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
        this.totalAmount = getTotalAmount();
    }

    public double getExtendedWarrantyCost() {
        return extendedWarrantyCost;
    }

    public void setExtendedWarrantyCost(double extendedWarrantyCost) {
        this.extendedWarrantyCost = extendedWarrantyCost;
        this.totalAmount = getTotalAmount();
    }
    
    
    public void applyDiscount(String promotionName, double discountAmount) {
        this.appliedPromotionName = promotionName;
        this.discountAmount = discountAmount;
        this.totalAmount = getTotalAmount();
    }
    /**
     * Generates a simple text receipt for the sale showing subtotal,
     * applied promotion discount, extended warranty cost, and final total.
     *
     * @return the receipt text
     */
    public String generateReceipt() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("Sale ID: ").append(id).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("Customer: ").append(customer != null ? customer.getFullName() : "-").append("\n");
        sb.append("Seller: ").append(seller != null ? seller.getFullName() : "-").append("\n");
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
        sb.append("Costo de garantías extendidas: ").append(extendedWarrantyCost).append("\n");
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
    
    /**
     * Returns the proportional discount ratio applied over the product subtotal
     * ({@code discountAmount / subtotal}), or {@code 0.0} when subtotal is zero.
     *
     * @return the discount ratio between 0.0 and 1.0
     */
    public double getDiscountRatio() {
        if (subtotal > 0) {
            return discountAmount / subtotal;
        }
        return 0.0;
    }

    /**
     * Checks whether the sale is still within the 30-day return period.
     *
     * @return true if the sale can be returned; otherwise false
     */
    public boolean canBeReturned() {
        
        long daysPassed = ChronoUnit.DAYS.between(date, LocalDate.now());

        return daysPassed >= 0 && daysPassed <= 30;
    
    }

    /**
     * Finds a product within this sale by its identifier.
     *
     * @param productId the identifier of the product to search for
     * @return the matching {@link Product}, or {@code null} if not found in this sale
     */
    public Product findProductById(String productId) {
        if (productId == null || products == null) {
            return null;
        }
        for (Product product : products) {
            if (product != null && productId.equals(product.getId())) {
                return product;
            }
        }
        return null;
    }
    
}

