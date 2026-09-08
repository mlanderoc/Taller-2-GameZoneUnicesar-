
package com.gamezone.model;

/**
 * Represents a customer in the GameZone system.
 *
 * <p>The Customer class extends the {@link Person} class and
 * contains additional information specific to customers, such
 * as their email address and purchase history.</p>
 *
 * @author GameZone
 * @version 1.0
 */
public class Customer extends Person {

    /**
     * Email address of the customer.
     */
    private String email;

    /**
     * Purchase history of the customer.
     */
    private String purchasehistory;

    /**
     * Default constructor of the Customer class.
     *
     * <p>Creates a Customer object without initializing
     * its specific attributes.</p>
     */
    public Customer() {
    }

    /**
     * Parameterized constructor that initializes a customer
     * with their email address and purchase history.
     *
     * @param email email address of the customer
     * @param purchasehistory purchase history of the customer
     */
    public Customer(String email, String purchasehistory) {
        this.email = email;
        this.purchasehistory = purchasehistory;
    }

    /**
     * Returns the customer's email address.
     *
     * @return the customer's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the customer's email address.
     *
     * @param email new email address of the customer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the customer's purchase history.
     *
     * @return the customer's purchase history
     */
    public String getPurchasehistory() {
        return purchasehistory;
    }

    /**
     * Sets the customer's purchase history.
     *
     * @param purchasehistory new purchase history of the customer
     */
    public void setPurchasehistory(String purchasehistory) {
        this.purchasehistory = purchasehistory;
    }
}

