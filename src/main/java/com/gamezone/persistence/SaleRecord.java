
package com.gamezone.persistence;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class SaleRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String saleId;
    private LocalDate date;
    private String customerId;
    private String sellerId;
    private List<String> productIds;
    private double totalAmount;
    
    public SaleRecord(String saleId, LocalDate date, String customerId,
                       String sellerId, List<String> productIds, double totalAmount) {
        this.saleId = saleId;
        this.date = date;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.productIds = productIds;
        this.totalAmount = totalAmount;
    }
    //GETTERS and SETTERS
    public String getSaleId() {
        return saleId;
    }
    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getSellerId() {
        return sellerId;
    }
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
