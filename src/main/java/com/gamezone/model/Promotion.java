
package com.gamezone.model;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a promotion that can apply a discount to a sale.
 * A promotion has a validity period and defines a discount
 * calculation strategy for its concrete subclasses.
 */
public abstract class Promotion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name; 
    private LocalDate startDate; 
    private LocalDate endDate; 

    /**
     * Creates a promotion with its common information and validity period.
     *
     * @param id the promotion identifier
     * @param name the promotion name
     * @param startDate the date when the promotion becomes active
     * @param endDate the date when the promotion expires
     */
    public Promotion(String id, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the promotion identifier.
     *
     * @return the promotion identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the promotion identifier.
     *
     * @param id the new promotion identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the promotion name.
     *
     * @return the promotion name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the promotion name.
     *
     * @param name the new promotion name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the date when the promotion becomes active.
     *
     * @return the promotion start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the date when the promotion becomes active.
     *
     * @param startDate the new promotion start date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the date when the promotion expires.
     *
     * @return the promotion end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the date when the promotion expires.
     *
     * @param endDate the new promotion end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    /**
     * Checks whether this promotion is active on the specified date.
     * The start and end dates are included in the validity period.
     *
     * @param date the date to evaluate
     * @return true if the promotion is active on the given date;
     *         false otherwise
     */
    public boolean isActive(LocalDate date){
        
        return !date.isBefore(startDate) && !date.isAfter(endDate);    
    }
    
    /**
     * Calculates the monetary discount that this promotion applies
     * to the specified sale.
     *
     * @param sale the sale used to calculate the discount
     * @return the discount amount for the given sale
     */
    public abstract double calculateDiscount(Sale sale); 
    
}
