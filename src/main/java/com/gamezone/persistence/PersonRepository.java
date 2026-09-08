
package com.gamezone.persistence;

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
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    /**
     * Loads all customers from the customer data file.
     *
     * <p>If the file does not exist, an empty list is returned.
     * If an error occurs while reading or deserializing the file,
     * an empty list is also returned.</p>
     *
     * @return list containing all saved customers, or an empty list
     *         if the file does not exist or cannot be read
     */
    @SuppressWarnings("unchecked")
    public List<Customer> loadAllCustomers() {
        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(CUSTOMER_FILE))) {

            return (List<Customer>) input.readObject();

        } catch (FileNotFoundException e) {
            return new ArrayList<>();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading customers: " + e.getMessage());
            return new ArrayList<>();
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
            System.out.println("Error saving sellers: " + e.getMessage());
        }
    }

    /**
     * Loads all sellers from the seller data file.
     *
     * <p>If the file does not exist, an empty list is returned.
     * If an error occurs while reading or deserializing the file,
     * an empty list is also returned.</p>
     *
     * @return list containing all saved sellers, or an empty list
     *         if the file does not exist or cannot be read
     */
    @SuppressWarnings("unchecked")
    public List<Seller> loadAllSellers() {
        try (ObjectInputStream input =
                     new ObjectInputStream(new FileInputStream(SELLER_FILE))) {

            return (List<Seller>) input.readObject();

        } catch (FileNotFoundException e) {
            return new ArrayList<>();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading sellers: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
