
package com.gamezone.model;

/**
 * Represents a seller in the GameZone system.
 *
 * <p>The Seller class extends the {@link Person} class and contains
 * additional information related to employees who work as sellers,
 * including their employee code and work shift.</p>
 *
 * @author GameZone
 * @version 1.0
 */
public class Seller extends Person {

    /**
     * Unique employee code assigned to the seller.
     */
    private String employeecode;

    /**
     * Work shift assigned to the seller.
     */
    private String shift;

    /**
     * Default constructor of the Seller class.
     *
     * <p>Creates a Seller object without initializing
     * its specific attributes.</p>
     */
    public Seller() {
    }

    /**
     * Parameterized constructor that initializes a seller
     * with an employee code and work shift.
     *
     * @param employeecode unique employee code of the seller
     * @param shift work shift assigned to the seller
     */

    public Seller(String id, String firstName, String lastName, String phone,String employeecode, String shift) {
        super(id, firstName, lastName, phone);
        this.employeecode = employeecode;
        this.shift = shift;
    }
    

    /**
     * Returns the employee code of the seller.
     *
     * @return the seller's employee code
     */
    public String getEmployeecode() {
        return employeecode;
    }

    /**
     * Sets the employee code of the seller.
     *
     * @param employeecode new employee code of the seller
     */
    public void setEmployeecode(String employeecode) {
        this.employeecode = employeecode;
    }

    /**
     * Returns the work shift assigned to the seller.
     *
     * @return the seller's work shift
     */
    public String getShift() {
        return shift;
    }

    /**
     * Sets the work shift assigned to the seller.
     *
     * @param shift new work shift assigned to the seller
     */
    public void setShift(String shift) {
        this.shift = shift;
    }
}

