
package com.gamezone.service;

import gamezone.model.Customer;
import gamezone.model.Seller;
import gamezone.persistence.PersonRepository;

import java.util.List;

/**
 * Service class responsible for managing customers and sellers
 * in the GameZone system.
 *
 * <p>This class provides business logic for registering customers,
 * searching for customers and sellers by their IDs, and retrieving
 * lists of all registered customers and sellers.</p>
 *
 * <p>The service uses {@link PersonRepository} to handle data
 * persistence.</p>
 *
 * @author GameZone
 * @version 1.0
 */
public class PersonService {

    /**
     * Repository used to store and retrieve customer and seller data.
     */
    private final PersonRepository repository;

    /**
     * Creates a new PersonService and initializes the repository.
     */
    public PersonService() {
        this.repository = new PersonRepository();
    }

    /**
     * Registers a new customer in the system.
     *
     * <p>The method loads the existing customers, adds the new customer
     * to the list, and saves the updated list through the repository.</p>
     *
     * @param customer customer to be registered
     */
    public void registerCustomer(Customer customer) {
        List<Customer> customers = repository.loadAllCustomers();

        customers.add(customer);

        repository.saveAllCustomers(customers);
    }

    /**
     * Searches for a customer by their ID.
     *
     * <p>The method loads all registered customers and compares
     * their IDs with the provided ID.</p>
     *
     * @param id ID of the customer to search for
     * @return the customer matching the specified ID, or {@code null}
     *         if no customer is found
     */
    public Customer findCustomerById(String id) {
        List<Customer> customers = repository.loadAllCustomers();

        for (Customer customer : customers) {
            if (String.valueOf(customer.getId()).equals(id)) {
                return customer;
            }
        }

        return null;
    }

    /**
     * Searches for a seller by their ID.
     *
     * <p>The method loads all registered sellers and compares
     * their IDs with the provided ID.</p>
     *
     * @param id ID of the seller to search for
     * @return the seller matching the specified ID, or {@code null}
     *         if no seller is found
     */
    public Seller findSellerById(String id) {
        List<Seller> sellers = repository.loadAllSellers();

        for (Seller seller : sellers) {
            if (String.valueOf(seller.getId()).equals(id)) {
                return seller;
            }
        }

        return null;
    }

    /**
     * Retrieves all registered customers.
     *
     * @return list containing all registered customers
     */
    public List<Customer> listAllCustomers() {
        return repository.loadAllCustomers();
    }

    /**
     * Retrieves all registered sellers.
     *
     * @return list containing all registered sellers
     */
    public List<Seller> listAllSellers() {
        return repository.loadAllSellers();
    }
}
