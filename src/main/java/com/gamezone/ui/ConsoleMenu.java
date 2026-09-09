
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
import com.gamezone.service.PromotionService;

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
    private PromotionService promotionService;
    private Scanner scanner;

    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService,PromotionService promotionService ) {
        this.productService = productService;
        this.personService = personService;
        this.promotionService = promotionService; 
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
            case "1":
                showProductMenu();
                break;
            case "2":
                showPeopleMenu();
                break;
            case "3":
                showSalesMenu();
                break;
            case "4":
                showPromotionMenu();
                break;
            case "0":
                running = false;
                printMessage("Gracias por usar GameZone Unicesar.");
                break;
            default:
                printError("Opción invalida. Intente nuevamente.");
                break;
        }
    }

    scanner.close();
}

    private void printMainMenu() {
    printHeader("GAMEZONE UNICESAR");
    System.out.println("       Sistema de gestión de videojuegos");
    printLine();
    System.out.println("  [1] Gestión de productos");
    System.out.println("  [2] Gestión de personas");
    System.out.println("  [3] Gestión de ventas");
    System.out.println("  [4] Gestión de promociones");
    System.out.println("  [0] Salir");
    printLine();
    System.out.print("Seleccione una opción: ");
}
    
    private void showProductMenu() {
    boolean inProductMenu = true;

    while (inProductMenu) {
        printHeader("GESTIÓN DE PRODUCTOS");
        System.out.println("  [1] Registrar videojuego");
        System.out.println("  [2] Registrar consola");
        System.out.println("  [3] Listar todos los productos");
        System.out.println("  [0] Volver al menú principal");
        printLine();
        System.out.print("Seleccione una opción: ");

        String option = scanner.nextLine().trim();

        switch (option) {
            case "1":
                registerVideoGame();
                pause();
                break;
            case "2":
                registerConsole();
                pause();
                break;
            case "3":
                listAllProducts();
                pause();
                break;
            case "0":
                inProductMenu = false;
                break;
            default:
                printError("Opción inválida. Intente nuevamente.");
                pause();
                break;
        }
    }
}

private void showPeopleMenu() {
    boolean inPeopleMenu = true;

    while (inPeopleMenu) {
        printHeader("GESTIÓN DE PERSONAS");
        System.out.println("  [1] Registrar cliente");
        System.out.println("  [2] Listar clientes");
        System.out.println("  [3] Listar vendedores");
        System.out.println("  [0] Volver al menú principal");
        printLine();
        System.out.print("Seleccione una opción: ");

        String option = scanner.nextLine().trim();

        switch (option) {
            case "1":
                registerCustomer();
                pause();
                break;
            case "2":
                listAllCustomers();
                pause();
                break;
            case "3":
                listAllSellers();
                pause();
                break;
            case "0":
                inPeopleMenu = false;
                break;
            default:
                printError("Opción inválida. Intente nuevamente.");
                pause();
                break;
        }
    }
}

private void showSalesMenu() {
    boolean inSalesMenu = true;

    while (inSalesMenu) {
        printHeader("GESTIÓN DE VENTAS");
        System.out.println("  [1] Registrar venta");
        System.out.println("  [2] Ver todas las ventas");
        System.out.println("  [3] Ver ventas por cliente");
        System.out.println("  [4] Ver ventas por vendedor");
        System.out.println("  [0] Volver al menú principal");
        printLine();
        System.out.print("Seleccione una opción: ");

        String option = scanner.nextLine().trim();

        switch (option) {
            case "1":
                registerSale();
                pause();
                break;
            case "2":
                viewAllSales();
                pause();
                break;
            case "3":
                viewSalesByCustomer();
                pause();
                break;
            case "4":
                viewSalesBySeller();
                pause();
                break;
            case "0":
                inSalesMenu = false;
                break;
            default:
                printError("Opción inválida. Intente nuevamente.");
                pause();
                break;
        }
    }
}

private void showPromotionMenu() {
    boolean inPromotionMenu = true;

    while (inPromotionMenu) {
        printHeader("GESTIÓN DE PROMOCIONES");
        System.out.println("  [1] Registrar promoción por porcentaje");
        System.out.println("  [2] Registrar promoción por categoría");
        System.out.println("  [3] Registrar promoción por volumen");
        System.out.println("  [4] Listar todas las promociones");
        System.out.println("  [5] Listar promociones vigentes");
        System.out.println("  [0] Volver al menú principal");
        printLine();
        System.out.print("Seleccione una opción: ");

        String option = scanner.nextLine().trim();

        switch (option) {
            case "1":
                registerPercentageDiscount();
                pause();
                break;
            case "2":
                registerCategoryDiscount();
                pause();
                break;
            case "3":
                registerBulkPurchaseDiscount();
                pause();
                break;
            case "4":
                listAllPromotions();
                pause();
                break;
            case "5":
                listActivePromotions();
                pause();
                break;
            case "0":
                inPromotionMenu = false;
                break;
            default:
                printError("Opción inválida. Intente nuevamente.");
                pause();
                break;
        }
    }
}

    // ===== Products =====

    private void registerVideoGame() {
        System.out.println("==========================================");
        System.out.print("Id: ");
        String id = scanner.nextLine().trim();
        System.out.print("Titulo: ");
        String title = scanner.nextLine().trim();
        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Unidades: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Plataforma: ");
        String platform = scanner.nextLine().trim();
        System.out.print("Genero: ");
        String genre = scanner.nextLine().trim();
        System.out.print("Clasificacion por edad: ");
        String ageRating = scanner.nextLine().trim();

        productService.registerVideoGame(id, title, price, stock, platform, genre, ageRating);
        System.out.println("Videojuego registrado correctamente.");
    }

    private void registerConsole() {
        System.out.println("==========================================");
        System.out.print("Id: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nombre: ");
        String title = scanner.nextLine().trim();
        System.out.print("Precio: ");
        double price = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Unidades: ");
        int stock = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Marca: ");
        String brand = scanner.nextLine().trim();
        System.out.print("Modelo: ");
        String model = scanner.nextLine().trim();
        System.out.print("Generacion: ");
        String generation = scanner.nextLine().trim();
        
        System.out.println("--------------------------------------------");
        productService.registerConsole(id, title, price, stock, brand, model, generation);
        System.out.println("Consola registrada correctamente.");
    }

    private void listAllProducts() {
        System.out.println("==========================================");
        List<Product> products = productService.listAllProducts();
        if (products.isEmpty()) {
            System.out.println("Aún no hay productos registrados.");
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
        System.out.print("Nombre: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Apellido: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Telefono: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        personService.registerCustomer(id, firstName, lastName, phone, email);
        System.out.println("Cliente registrado con éxito.");
    }

    private void listAllCustomers() {
        System.out.println("==========================================");
        List<Customer> customers = personService.listAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("Aún no hay clientes registrados.");
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
            System.out.println("No hay vendedores registrados.");
            return;
        }
        for (Seller seller : sellers) {
            System.out.println(seller.getFullName() + " - " + seller.getId());
        }
    }

    // ===== Sales =====

    private void registerSale() {
        System.out.println("==========================================");
        System.out.print("Empleado id: ");
        String customerId = scanner.nextLine().trim();
        Customer customer = personService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.print("Vendedor id: ");
        String sellerId = scanner.nextLine().trim();
        Seller seller = personService.findSellerById(sellerId);
        if (seller == null) {
            System.out.println("Vendedor no encontrado");
            return;
        }

        List<Product> products = new ArrayList<>();
        System.out.println("Ingrese los ID de producto uno por uno. Escriba «Fin» al terminar.");
        while (true) {
            System.out.print("ID del producto (o «Fin»): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Fin")) {
                break;
            }
            Product product = productService.findById(input);
            if (product == null) {
                System.out.println("Producto no encontrado, intentalo de nuevo.");
                continue;
            }
            products.add(product);
            System.out.println("Agregado: " + product.getDescription());
        }

        try {
            Sale sale = saleService.registerSale(customer, seller, products);
            System.out.println("Venta registrada con éxito.");
            System.out.println(sale.generateReceipt());
        } catch (IllegalArgumentException exception) {
            System.out.println("No se pudo registrar la venta: " + exception.getMessage());
        }
    }

    private void viewAllSales() {
        System.out.println("==========================================");
        List<Sale> sales = saleService.viewAllSales();
        if (sales.isEmpty()) {
            System.out.println("Aún no se han registrado ventas.");
            return;
        }
        for (Sale sale : sales) {
            System.out.println(sale.generateReceipt());
            System.out.println("-----");
        }
    }

    private void viewSalesByCustomer() {
        System.out.println("==========================================");
        System.out.print("Cliente id: ");
        String customerId = scanner.nextLine().trim();
        Customer customer = personService.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        List<Sale> sales = saleService.viewSalesByCustomer(customer);
        if (sales.isEmpty()) {
            System.out.println("Este cliente no tiene compras todavía.");
            return;
        }
        for (Sale sale : sales) {
            System.out.println(sale.generateReceipt());
            System.out.println("-----");
        }
    }

    private void viewSalesBySeller() {
        System.out.println("==========================================");
        System.out.print("Vendedor id: ");
        String sellerId = scanner.nextLine().trim();
        Seller seller = personService.findSellerById(sellerId);
        if (seller == null) {
            System.out.println("Vendedor no encontrado.");
            return;
        }
        List<Sale> sales = saleService.viewSalesBySeller(seller);
        if (sales.isEmpty()) {
            System.out.println("Este vendedor aún no tiene ventas.");
            return;
        }
        for (Sale sale : sales) {
            System.out.println(sale.generateReceipt());
            System.out.println("-----");
        }
    }
    
    private void registerPercentageDiscount() {
    printMessage("Registro de promoción por porcentaje pendiente de integración.");
}

private void registerCategoryDiscount() {
    printMessage("Registro de promoción por categoría pendiente de integración.");
}

private void registerBulkPurchaseDiscount() {
    printMessage("Registro de promoción por volumen pendiente de integración.");
}

private void listAllPromotions() {
    printMessage("Listado de promociones pendiente de integración.");
}

private void listActivePromotions() {
    printMessage("Listado de promociones vigentes pendiente de integración.");
}
    
  private void printHeader(String title) {
    System.out.println();
    System.out.println("╔════════════════════════════════════════════╗");
    System.out.printf("║ %-42s ║%n", title);
    System.out.println("╚════════════════════════════════════════════╝");
}

private void printLine() {
    System.out.println("──────────────────────────────────────────────");
}

private void printMessage(String message) {
    System.out.println();
    System.out.println("  ✓ " + message);
}

private void printError(String message) {
    System.out.println();
    System.out.println("  ✗ " + message);
}

private void pause() {
    System.out.println();
    System.out.print("Presione Enter para continuar...");
    scanner.nextLine();
}
}
