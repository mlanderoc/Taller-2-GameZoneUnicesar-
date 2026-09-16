
package com.gamezone.model;

import java.time.LocalDate;
import java.util.List;

public class Return {
 
    private String id; 
    private LocalDate returnDate;
    private Sale originalSale;
    private List<Product> returnedProducts;
    private String reason;
    private double refundAmount;

    public Return(String id, LocalDate returnDate, Sale originalSale, List<Product> returnedProducts, String reason, double refundAmount) {
        this.id = id;
        this.returnDate = returnDate;
        this.originalSale = originalSale;
        this.returnedProducts = returnedProducts;
        this.reason = reason;
        this.refundAmount = calculateRefundAmount();
    }

    public String getId() {
        return id;
    }

  
    public LocalDate getReturnDate() {
        return returnDate;
    }


    public Sale getOriginalSale() {
        return originalSale;
    }

    public List<Product> getReturnedProducts() {
        return returnedProducts;
    }

    public String getReason() {
        return reason;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public double calculateRefundAmount() {
        double total = 0.0;

        for (Product product : returnedProducts) {
            total += product.getPrice();
        }

        this.refundAmount = total;
        return total;
    }
    
    public String generateReturnReceipt() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID de devolución: ").append(id).append("\n");
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
