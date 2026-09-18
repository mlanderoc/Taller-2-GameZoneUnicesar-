
package com.gamezone.persistence;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Plain data-transfer object representing the persisted form of a Warranty.
 * Stores only the ids of the associated product and sale, instead of full
 * domain objects, to avoid duplicating data already held by ProductRepository
 * and SaleRecord.
 */
public class WarrantyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String warrantyId;
    private String productId;
    private String saleId;
    private String warrantyType;
    private LocalDate startDate;
    private LocalDate endDate;

    public WarrantyRecord(String warrantyId, String productId, String saleId,
            String warrantyType, LocalDate startDate, LocalDate endDate) {
        this.warrantyId = warrantyId;
        this.productId = productId;
        this.saleId = saleId;
        this.warrantyType = warrantyType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getWarrantyId() {
        return warrantyId;
    }

    public void setWarrantyId(String warrantyId) {
        this.warrantyId = warrantyId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getWarrantyType() {
        return warrantyType;
    }

    public void setWarrantyType(String warrantyType) {
        this.warrantyType = warrantyType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
