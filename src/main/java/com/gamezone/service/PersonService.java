package com.gamezone.service;


import gamezone.model.Customer;
import gamezone.model.Seller;
import gamezone.persistence.PersonRepository;

import java.util.List;

public class PersonService {

    private final PersonRepository repository;

    public PersonService() {
        this.repository = new PersonRepository();
    }

    public void registerCustomer(Customer customer) {
        List<Customer> customers = repository.loadAllCustomers();

        customers.add(customer);

        repository.saveAllCustomers(customers);
    }

    public Customer findCustomerById(String id) {
        List<Customer> customers = repository.loadAllCustomers();

        for (Customer customer : customers) {
            if (String.valueOf(customer.getId()).equals(id)) {
                return customer;
            }
        }

        return null;
    }

    public Seller findSellerById(String id) {
        List<Seller> sellers = repository.loadAllSellers();

        for (Seller seller : sellers) {
            if (String.valueOf(seller.getId()).equals(id)) {
                return seller;
            }
        }

        return null;
    }

    public List<Customer> listAllCustomers() {
        return repository.loadAllCustomers();
    }

    public List<Seller> listAllSellers() {
        return repository.loadAllSellers();
    }
}