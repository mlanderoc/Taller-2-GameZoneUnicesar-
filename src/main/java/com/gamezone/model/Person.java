
package com.gamezone.model;

import java.io.Serializable;

/**
 * Represents a person within the GameZone system.
 *
 * <p>This is an abstract base class that provides common attributes
 * and methods for entities that represent people, such as customers,
 * employees, or other users of the system.</p>
 *
 * <p>The class implements {@link Serializable}, allowing its objects
 * to be serialized for storage or data transfer.</p>
 *
 * @author GameZone
 * @version 1.0
 */
public abstract class Person implements Serializable {

    /**
     * Unique identifier of the person.
     */
    protected String id;

    /**
     * First name of the person.
     */
    protected String firstName;

    /**
     * Last name of the person.
     */
    protected String lastName;

    /**
     * Phone number of the person.
     */
    protected long phone;

    /**
     * Default constructor of the Person class.
     *
     * <p>Creates a Person object without initializing its attributes.</p>
     */
    public Person() {
    }

    /**
     * Parameterized constructor that initializes a person
     * with all their basic information.
     *
     * @param id unique identifier of the person
     * @param firstName first name of the person
     * @param lastName last name of the person
     * @param phone phone number of the person
     */
    public Person(String id, String firstName, String lastName, long phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    /**
     * Returns the person's unique identifier.
     *
     * @return the person's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the person's unique identifier.
     *
     * @param id new identifier of the person
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the person's first name.
     *
     * @return the person's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the person's first name.
     *
     * @param firstName new first name of the person
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the person's last name.
     *
     * @return the person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the person's last name.
     *
     * @param lastName new last name of the person
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the person's phone number.
     *
     * @return the person's phone number
     */
    public long getPhone() {
        return phone;
    }

    /**
     * Sets the person's phone number.
     *
     * @param phone new phone number of the person
     */
    public void setPhone(long phone) {
        this.phone = phone;
    }

    /**
     * Returns the person's full name by combining their first
     * and last names.
     *
     * @return the person's full name
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}

