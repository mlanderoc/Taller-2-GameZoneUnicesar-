
package gamezoneunicesar.persistence;

import gamezoneunicesar.model.Customer;
import gamezoneunicesar.model.Seller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {

    private final String CUSTOMER_FILE = "customers.dat";
    private final String SELLER_FILE = "sellers.dat";

    public void saveAllCustomers(List<Customer> customers) {
        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(CUSTOMER_FILE))) {

            output.writeObject(customers);

        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

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

    public void saveAllSellers(List<Seller> sellers) {
        try (ObjectOutputStream output =
                     new ObjectOutputStream(new FileOutputStream(SELLER_FILE))) {

            output.writeObject(sellers);

        } catch (IOException e) {
            System.out.println("Error saving sellers: " + e.getMessage());
        }
    }

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