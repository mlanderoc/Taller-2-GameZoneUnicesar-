
package com.gamezone.ui;

import com.gamezone.model.Console;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.model.VideoGame;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for the GameZone system.
 * Talks only to the service layer, never directly to persistence.
 */
public class ConsoleMenu {

    private ProductService productService;
    private PersonService personService;
    private SaleService saleService;
    private Scanner scanner;

    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main menu loop.
     */
    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1": registerVideoGame(); break;
                case "2": registerConsole(); break;
                case "3": listAllProducts(); break;
                case "4": registerCustomer(); break;
                case "5": listAllCustomers(); break;
                case "6": listAllSellers(); break;
                case "7": registerSale(); break;
                case "8": viewAllSales(); break;
                case "9": viewSalesByCustomer(); break;
                case "10": viewSalesBySeller(); break;
                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
        scanner.close();
    }

    private void printMainMenu() {
        System.out.println("\n===== GameZone Unicesar =====");
        System.out.println("1. Register video game");
        System.out.println("2. Register console");
        System.out.println("3. List all products");
        System.out.println("4. Register customer");
        System.out.println("5. List all customers");
        System.out.println("6. List all sellers");
        System.out.println("7. Register sale");
        System.out.println("8. View all sales");
        System.out.println("9. View sales by customer");
        System.out.println("10. View sales by seller");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    // ===== Products =====

    private void registerVideoGame() {
        System.out.println("==========================================");
        System.out.print("Id: ");
        String id = scanner.nextLine().trim();
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Platform: ");
        String platform = scanner.nextLine().trim();
        System.out.print("Genre: ");
        String genre = scanner.nextLine().trim();
        System.out.print("Age rating: ");
        String ageRating = scanner.nextLine().trim();

        productService.registerVideoGame(id, title, price, stock, platform, genre, ageRating);
        System.out.println("Video game registered successfully.");
    }

    private void registerConsole() {
        System.out.println("==========================================");
        System.out.print("Id: ");
        String id = scanner.nextLine().trim();
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Brand: ");
        String brand = scanner.nextLine().trim();
        System.out.print("Model: ");
        String model = scanner.nextLine().trim();
        System.out.print("Generation: ");
        String generation = scanner.nextLine().trim();
        
        System.out.println("--------------------------------------------");
        productService.registerConsole(id, title, price, stock, brand, model, generation);
        System.out.println("Console registered successfully.");
    }

    private void listAllProducts() {
        System.out.println("==========================================");
        List<Product> products = productService.listAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products registered yet.");
            return;
        }
        for (Product product : products) {
            System.out.println(product.getDescription());
        }
    }

    // ===== People =====

    private void registerCustomer() {
        System.out.println("==========================================");
        System.out.print("Id: ");
        String id = scanner.nextLine().trim();
        System.out.print("First name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        personService.registerCustomer(id, firstName, lastName, phone, email);
        System.out.println("Customer registered successfully.");
    }

    private void listAllCustomers() {
        System.out.println("==========================================");
        List<Customer> customers = personService.listAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers registered yet.");
            return;
        }
        for (Customer customer : customers) {
            System.out.println(customer.getFullName() + " - " + customer.getId());
        }
    }

    private void listAllSellers() {
        System.out.println("==========================================");
        List<Seller> sellers = personService.listAllSellers();
        if (sellers.isEmpty()) {
            System.out.println("No sellers registered.");
            return;
        }
        for (Seller seller : sellers) {
            System.out.println(seller.getFullName() + " - " + seller.getId());
        }
    }

    // ===== Sales =====

    private void registerSale() {
        System.out.println("==========================================");
        System.out.print("Customer id: ");
        String customerId = scanner.nextLine().trim();
        Customer customer = personService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.print("Seller id: ");
        String sellerId = scanner.nextLine().trim();
        Seller seller = personService.findSellerById(sellerId);
        if (seller == null) {
            System.out.println("Seller not found.");
            return;
        }

        List<Product> products = new ArrayList<>();
        System.out.println("Enter product ids one at a time. Type 'done' when finished.");
        while (true) {
            System.out.print("Product id (or 'done'): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("done")) {
                break;
            }
            Product product = productService.findById(input);
            if (product == null) {
                System.out.println("Product not found, try again.");
                continue;
            }
            products.add(product);
            System.out.println("Added: " + product.getDescription());
        }

        try {
            Sale sale = saleService.registerSale(customer, seller, products);
            System.out.println("Sale registered successfully.");
            System.out.println(sale.generateReceipt());
        } catch (IllegalArgumentException exception) {
            System.out.println("Could not register sale: " + exception.getMessage());
        }
    }

    private void viewAllSales() {
        System.out.println("==========================================");
        List<Sale> sales = saleService.viewAllSales();
        if (sales.isEmpty()) {
            System.out.println("No sales registered yet.");
            return;
        }
        for (Sale sale : sales) {
            System.out.println(sale.generateReceipt());
            System.out.println("-----");
        }
    }

    private void viewSalesByCustomer() {
        System.out.println("==========================================");
        System.out.print("Customer id: ");
        String customerId = scanner.nextLine().trim();
        Customer customer = personService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }
        List<Sale> sales = saleService.viewSalesByCustomer(customer);
        if (sales.isEmpty()) {
            System.out.println("This customer has no purchases yet.");
            return;
        }
        for (Sale sale : sales) {
            System.out.println(sale.generateReceipt());
            System.out.println("-----");
        }
    }

    private void viewSalesBySeller() {
        System.out.println("==========================================");
        System.out.print("Seller id: ");
        String sellerId = scanner.nextLine().trim();
        Seller seller = personService.findSellerById(sellerId);
        if (seller == null) {
            System.out.println("Seller not found.");
            return;
        }
        List<Sale> sales = saleService.viewSalesBySeller(seller);
        if (sales.isEmpty()) {
            System.out.println("This seller has no sales yet.");
            return;
        }
        for (Sale sale : sales) {
            System.out.println(sale.generateReceipt());
            System.out.println("-----");
        }
    }
}
