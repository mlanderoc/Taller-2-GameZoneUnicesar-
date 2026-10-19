
package com.gamezone.persistence;

import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.Customer;
import com.gamezone.model.Seller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for the persistence of customers and sellers
 * in the GameZone system.
 *
 * <p>This class uses Java serialization to save and load lists of
 * {@link Customer} and {@link Seller} objects from binary files.</p>
 *
 * <p>Customer data is stored in the {@code customers.dat} file,
 * while seller data is stored in the {@code sellers.dat} file.</p>
 *
 * @author GameZone
 * @version 1.0
 */
public class PersonRepository {

    /**
     * File used to store customer data.
     */
    private final String CUSTOMER_FILE = "customers.dat";

    /**
     * File used to store seller data.
     */
    private final String SELLER_FILE = "sellers.dat";

    /**
     * Saves all customers to the customer data file.
     *
     * <p>The provided list is serialized and written to the
     * {@code customers.dat} file.</p>
     *
     * @param customers list of customers to be saved
     */
    public void saveAllCustomers(List<Customer> customers) {
        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(CUSTOMER_FILE))) {

            output.writeObject(customers);

        } catch (IOException e) {
            throw new PersistenceException("Error de escritura en el archivo " + CUSTOMER_FILE, e);
        }
    }

    /**
     * Loads all customers from the customer data file.
     *
     * <p>If the file does not exist, an empty list is returned.</p>
     *
     * @return list containing all saved customers, or an empty list
     *         if the file does not exist
     */
    @SuppressWarnings("unchecked")
    public List<Customer> loadAllCustomers() {
        File file = new File(CUSTOMER_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (List<Customer>) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenceException("Error de lectura en el archivo " + CUSTOMER_FILE, e);
        }
    }

    /**
     * Saves all sellers to the seller data file.
     *
     * <p>The provided list is serialized and written to the
     * {@code sellers.dat} file.</p>
     *
     * @param sellers list of sellers to be saved
     */
    public void saveAllSellers(List<Seller> sellers) {
        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(SELLER_FILE))) {

            output.writeObject(sellers);

        } catch (IOException e) {
            throw new PersistenceException("Error de escritura en el archivo " + SELLER_FILE, e);
        }
    }

    /**
     * Loads all sellers from the seller data file.
     *
     * <p>If the file does not exist, an empty list is returned.</p>
     *
     * @return list containing all saved sellers, or an empty list
     *         if the file does not exist
     */
    @SuppressWarnings("unchecked")
    public List<Seller> loadAllSellers() {
        File file = new File(SELLER_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(file))) {

            return (List<Seller>) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenceException("Error de lectura en el archivo " + SELLER_FILE, e);
        }
    }
}
