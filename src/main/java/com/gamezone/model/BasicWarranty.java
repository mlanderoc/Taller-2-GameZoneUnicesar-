
package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a basic warranty for a product.
 * It lasts six months and has no additional cost.
 */
public class BasicWarranty extends Warranty {

    /**
     * Creates a new basic warranty.
     *
     * @param id unique identifier of the warranty
     * @param product product covered by the warranty
     * @param sale sale associated with the warranty
     * @param startDate start date of the warranty
     */
    public BasicWarranty(String id, Product product, Sale sale, LocalDate startDate){
        super(id, product, sale, startDate);
    }
    
    /**
     * Returns the duration of a basic warranty in months.
     *
     * @return 6 months
     */
    @Override
    public int getDurationInMonths() {
        return 6;
    }

    /**
     * Returns the basic warranty type name.
     *
     * @return basic warranty type name
     */
    @Override
    public String getWarrantyType() {
        return "Garantía Básica";
    }

    /**
     * Returns the additional cost of a basic warranty.
     *
     * @return 0.0 because the basic warranty is free
     */
    @Override
    public double getAdditionalCost() {
        return 0.0;
    }
    
    
}
