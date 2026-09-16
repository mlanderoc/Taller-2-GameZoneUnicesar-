
package com.gamezone.model;

import java.time.LocalDate;

public abstract class Warranty {
    
    private String id;
    private Product product;
    private Sale sale;
    private LocalDate startDate;
    private LocalDate endDate;

    public Warranty(String id, Product product, Sale sale, LocalDate startDate) {
        this.id = id;
        this.product = product;
        this.sale = sale;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(getDurationInMonths());
    }

    public String getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }


    public Sale getSale() {
        return sale;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public abstract int getDurationInMonths();

    public abstract String getWarrantyType();

    public abstract double getAdditionalCost();

    public boolean isActive(LocalDate date) {

        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
    
    public String generateWarrantyCertificate() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID de garantía: ").append(id).append("\n");
        sb.append("Tipo de garantía: ").append(getWarrantyType()).append("\n");
        sb.append("Producto: ").append(product.getTitle()).append("\n");
        sb.append("ID de venta: ").append(sale.getId()).append("\n");
        sb.append("Fecha de inicio: ").append(startDate).append("\n");
        sb.append("Fecha de vencimiento: ").append(endDate).append("\n");
        sb.append("Costo adicional: ").append(getAdditionalCost());

        return sb.toString();
    }
    
}
