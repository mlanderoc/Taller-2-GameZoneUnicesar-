
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
    private double warrantyRefundAmount;
    private double refundAmount;

    /**
     * Creates a new return and calculates the refund amount
     * from the returned products.
     *
     * @param returnId unique identifier of the return
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
        this.warrantyRefundAmount = 0.0;
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
     * Returns the refunded amount corresponding to cancelled extended warranties.
     *
     * @return warranty refund amount
     */
    public double getWarrantyRefundAmount() {
        return warrantyRefundAmount;
    }

    /**
     * Sets the refunded amount corresponding to cancelled extended warranties.
     *
     * @param warrantyRefundAmount warranty refund amount
     */
    public void setWarrantyRefundAmount(double warrantyRefundAmount) {
        this.warrantyRefundAmount = warrantyRefundAmount;
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
     * Calculates the refund amount by applying the original sale's proportional
     * discount to each returned product: price * (1 - discount / subtotal),
     * plus any refunded extended warranty cost.
     *
     * @return calculated refund amount
     */
    public double calculateRefundAmount() {
        double discountRatio = getDiscountRatio();
        double total = 0.0;

        for (Product product : returnedProducts) {
            total += product.getPrice() * (1.0 - discountRatio);
        }

        total += warrantyRefundAmount;
        this.refundAmount = total;
        return total;
    }

    private double getDiscountRatio() {
        if (originalSale != null && originalSale.getSubtotal() > 0) {
            return originalSale.getDiscountAmount() / originalSale.getSubtotal();
        }
        return 0.0;
    }
    
    /**
     * Generates a formatted receipt with the return information, including
     * list price, proportional discount, refunded amount for each item,
     * and any refunded extended warranty amount.
     *
     * @return return receipt text
     */
    public String generateReturnReceipt() {
        StringBuilder sb = new StringBuilder();
        double discountRatio = getDiscountRatio();

        sb.append("ID de devolución: ").append(returnId).append("\n");
        sb.append("Fecha de devolución: ").append(returnDate).append("\n");
        sb.append("ID de venta original: ").append(originalSale != null ? originalSale.getId() : "-").append("\n");
        sb.append("Productos devueltos:\n");

        for (Product product : returnedProducts) {
            double listPrice = product.getPrice();
            double proportionalDiscount = listPrice * discountRatio;
            double itemRefund = listPrice - proportionalDiscount;
            sb.append(" - ")
                    .append(product.getTitle())
                    .append(" | Precio de lista: ")
                    .append(String.format(java.util.Locale.US, "%.2f", listPrice))
                    .append(" | Descuento proporcional: ")
                    .append(String.format(java.util.Locale.US, "%.2f", proportionalDiscount))
                    .append(" | Monto reembolsado: ")
                    .append(String.format(java.util.Locale.US, "%.2f", itemRefund))
                    .append("\n");
        }

        sb.append("Garantía extendida reembolsada: ").append(String.format(java.util.Locale.US, "%.2f", warrantyRefundAmount)).append("\n");
        sb.append("Motivo: ").append(reason).append("\n");
        sb.append("Monto reembolsado: ").append(String.format(java.util.Locale.US, "%.2f", refundAmount));

        return sb.toString();
    }
}
