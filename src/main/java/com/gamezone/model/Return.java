
package com.gamezone.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a product return associated with an original sale.
 * It stores the returned products, the return reason, and the refund amount.
 */
public class Return {
 
    private String returnId; 
    private LocalDate returnDate;
    private Sale originalSale;
    private List<Product> returnedProducts;
    private String reason;
    private double refundAmount;

    /**
     * Creates a new return and calculates the refund amount
     * from the returned products.
     *
     * @param id unique identifier of the return
     * @param returnDate date when the return was registered
     * @param originalSale sale associated with the return
     * @param returnedProducts products returned by the customer
     * @param reason reason for the return
     */
    public Return(String returnId, LocalDate returnDate, Sale originalSale, List<Product> returnedProducts, String reason) {
        this.returnId = returnId;
        this.returnDate = returnDate;
        this.originalSale = originalSale;
        this.returnedProducts = returnedProducts;
        this.reason = reason;
        this.refundAmount = calculateRefundAmount();
    }

     /**
     * Returns the unique identifier of the return.
     *
     * @return return identifier
     */
    public String getReturnId() {
        return returnId;
    }

    /**
     * Returns the date when the return was registered.
     *
     * @return return date
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }


    /**
     * Returns the original sale associated with this return.
     *
     * @return original sale
     */
    public Sale getOriginalSale() {
        return originalSale;
    }

    /**
     * Returns the products included in this return.
     *
     * @return list of returned products
     */
    public List<Product> getReturnedProducts() {
        return returnedProducts;
    }

    /**
     * Returns the reason for the return.
     *
     * @return return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns the amount refunded to the customer.
     *
     * @return refund amount
     */
    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }
    

    /**
     * Calculates the refund amount by summing the prices
     * of all returned products.
     *
     * @return calculated refund amount
     */
    public double calculateRefundAmount() {
        double total = 0.0;

        for (Product product : returnedProducts) {
            total += product.getPrice();
        }

        this.refundAmount = total;
        return total;
    }
    
    /**
     * Generates a formatted receipt with the return information.
     *
     * @return return receipt text
     */
    public String generateReturnReceipt() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID de devolución: ").append(returnId).append("\n");
        sb.append("Fecha de devolución: ").append(returnDate).append("\n");
        sb.append("ID de venta original: ").append(originalSale.getId()).append("\n");
        sb.append("Productos devueltos:\n");

        for (Product product : returnedProducts) {
            sb.append(" - ")
                    .append(product.getTitle())
                    .append(": ")
                    .append(product.getPrice())
                    .append("\n");
        }

        sb.append("Motivo: ").append(reason).append("\n");
        sb.append("Monto reembolsado: ").append(refundAmount);

        return sb.toString();
    }
    
    
}
