
package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents an extended warranty for a product.
 * It lasts twelve months and adds a cost based on the product price.
 */
public class ExtendedWarranty extends Warranty {

    /**
     * Creates a new extended warranty.
     *
     * @param id unique identifier of the warranty
     * @param product product covered by the warranty
     * @param sale sale associated with the warranty
     * @param startDate start date of the warranty
     */
    public ExtendedWarranty(String id,Product product,Sale sale,LocalDate startDate) {
        super(id, product, sale, startDate);
    }
    
    /**
     * Returns the duration of an extended warranty in months.
     *
     * @return 12 months
     */
    @Override
    public int getDurationInMonths() {
        return 12;
    }

    /**
     * Returns the extended warranty type name.
     *
     * @return extended warranty type name
     */
    @Override
    public String getWarrantyType() {
        return "Garantía Extendida";
    }

    /**
     * Calculates the additional cost of the extended warranty.
     *
     * @return ten percent of the covered product price
     */
    @Override
    public double getAdditionalCost() {
        return getProduct().getPrice() * 0.10;
    }
}
