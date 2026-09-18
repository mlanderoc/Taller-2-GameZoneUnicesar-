
package com.gamezone.persistence;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Plain data-transfer object representing the persisted form of a Return.
 * Stores only the id of the original sale and the ids of the returned
 * products, instead of full domain objects, to avoid duplicating data
 * already held by SaleRecord and ProductRepository.
 */
public class ReturnRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String returnId;
    private LocalDate returnDate;
    private String saleId;
    private List<String> productIds;
    private String reason;
    private double refundedAmount;

    public ReturnRecord(String returnId, LocalDate returnDate, String saleId,
                         List<String> productIds, String reason, double refundedAmount) {
        this.returnId = returnId;
        this.returnDate = returnDate;
        this.saleId = saleId;
        this.productIds = productIds;
        this.reason = reason;
        this.refundedAmount = refundedAmount;
    }

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(double refundedAmount) {
        this.refundedAmount = refundedAmount;
    }
}
