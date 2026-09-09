
package com.gamezone.persistence;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Plain data-transfer object representing the persisted form of a Sale.
 * Stores only ids of the related entities (customer, seller, products)
 * instead of full domain objects, avoiding duplicated or desynchronized
 * copies of those entities across separate data files.
 */
public class SaleRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private String saleId;
    private LocalDate date;
    private String customerId;
    private String sellerId;
    private List<String> productIds;
    private double subtotal;
    private String appliedPromotionName;
    private double discountAmount;
    
    
    
     /**
     * Creates a new SaleRecord.
     *
     * @param saleId      unique identifier of the sale
     * @param date        date the sale was made
     * @param customerId  id of the customer who made the purchase
     * @param sellerId    id of the seller who attended the sale
     * @param productIds  ids of the products included in the sale
     * @param  subtotal of the sale
     */
    
    public SaleRecord(String saleId, LocalDate date, String customerId,
                       String sellerId, List<String> productIds, double subtotal,String appliedPromotionName,double discountAmount) {
        this.saleId = saleId;
        this.date = date;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.productIds = productIds;
        this.subtotal = subtotal;
        this.appliedPromotionName = this.appliedPromotionName;
        this.discountAmount =this.discountAmount;
        
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

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
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
    
    
}
