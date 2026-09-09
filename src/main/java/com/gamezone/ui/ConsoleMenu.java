package com.gamezone.ui;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.SaleService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for the GameZone system.
 * Talks only to the service layer, never directly to persistence.
 */
public class ConsoleMenu {

    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;
    private final PromotionService promotionService;
    private final Scanner scanner;

    /**
     * Creates the console menu with the required application services.
     *
     * @param productService the service used to manage products
     * @param personService the service used to manage customers and sellers
     * @param saleService the service used to manage sales
     * @param promotionService the service used to manage promotions
     */
    public ConsoleMenu(ProductService productService,
                       PersonService personService,
                       SaleService saleService,
                       PromotionService promotionService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.promotionService = promotionService;
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
                    printError("Opcion invalida. Intente nuevamente.");
                    pause();
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Displays the main application menu.
     */
    private void printMainMenu() {
        printHeader("GAMEZONE UNICESAR");
        System.out.println("       Sistema de gestion de videojuegos");
        printLine();
        System.out.println("  [1] Gestion de productos");
        System.out.println("  [2] Gestion de personas");
        System.out.println("  [3] Gestion de ventas");
        System.out.println("  [4] Gestion de promociones");
        System.out.println("  [0] Salir");
        printLine();
        System.out.print("Seleccione una opcion: ");
    }

    /**
     * Displays and handles product-related options.
     */
    private void showProductMenu() {
        boolean inProductMenu = true;

        while (inProductMenu) {
            printHeader("GESTION DE PRODUCTOS");
            System.out.println("  [1] Registrar videojuego");
            System.out.println("  [2] Registrar consola");
            System.out.println("  [3] Listar todos los productos");
            System.out.println("  [0] Volver al menu principal");
            printLine();
            System.out.print("Seleccione una opcion: ");

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
                    printError("Opcion invalida. Intente nuevamente.");
                    pause();
                    break;
            }
        }
    }

    /**
     * Displays and handles people-related options.
     */
    private void showPeopleMenu() {
        boolean inPeopleMenu = true;

        while (inPeopleMenu) {
            printHeader("GESTION DE PERSONAS");
            System.out.println("  [1] Registrar cliente");
            System.out.println("  [2] Listar clientes");
            System.out.println("  [3] Listar vendedores");
            System.out.println("  [0] Volver al menu principal");
            printLine();
            System.out.print("Seleccione una opcion: ");

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
                    printError("Opcion invalida. Intente nuevamente.");
                    pause();
                    break;
            }
        }
    }

    /**
     * Displays and handles sale-related options.
     */
    private void showSalesMenu() {
        boolean inSalesMenu = true;

        while (inSalesMenu) {
            printHeader("GESTION DE VENTAS");
            System.out.println("  [1] Registrar venta");
            System.out.println("  [2] Ver todas las ventas");
            System.out.println("  [3] Ver ventas por cliente");
            System.out.println("  [4] Ver ventas por vendedor");
            System.out.println("  [0] Volver al menu principal");
            printLine();
            System.out.print("Seleccione una opcion: ");

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
                    printError("Opcion invalida. Intente nuevamente.");
                    pause();
                    break;
            }
        }
    }

    /**
     * Displays and handles promotion-related options.
     */
    private void showPromotionMenu() {
        boolean inPromotionMenu = true;

        while (inPromotionMenu) {
            printHeader("GESTION DE PROMOCIONES");
            System.out.println("  [1] Registrar promocion por porcentaje");
            System.out.println("  [2] Registrar promocion por categoria");
            System.out.println("  [3] Registrar promocion por volumen");
            System.out.println("  [4] Listar todas las promociones");
            System.out.println("  [5] Listar promociones vigentes");
            System.out.println("  [0] Volver al menu principal");
            printLine();
            System.out.print("Seleccione una opcion: ");

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
                    printError("Opcion invalida. Intente nuevamente.");
                    pause();
                    break;
            }
        }
    }

    /**
     * Registers a new video game.
     */
    private void registerVideoGame() {
        try {
            printHeader("REGISTRAR VIDEOJUEGO");

            System.out.print("ID: ");
            String id = scanner.nextLine().trim();

            System.out.print("Titulo: ");
            String title = scanner.nextLine().trim();

            System.out.print("Precio: ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Inventario: ");
            int stock = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Plataforma: ");
            String platform = scanner.nextLine().trim();

            System.out.print("Genero: ");
            String genre = scanner.nextLine().trim();

            System.out.print("Clasificacion por edad: ");
            String ageRating = scanner.nextLine().trim();

            productService.registerVideoGame(
                    id,
                    title,
                    price,
                    stock,
                    platform,
                    genre,
                    ageRating
            );

            printMessage("Videojuego registrado correctamente.");

        } catch (NumberFormatException exception) {
            printError("El precio y el inventario deben ser numeros validos.");
        } catch (IllegalArgumentException exception) {
            printError(exception.getMessage());
        }
    }

    /**
     * Registers a new console.
     */
    private void registerConsole() {
        try {
            printHeader("REGISTRAR CONSOLA");

            System.out.print("ID: ");
            String id = scanner.nextLine().trim();

            System.out.print("Titulo: ");
            String title = scanner.nextLine().trim();

            System.out.print("Precio: ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Inventario: ");
            int stock = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Marca: ");
            String brand = scanner.nextLine().trim();

            System.out.print("Modelo: ");
            String model = scanner.nextLine().trim();

            System.out.print("Generacion: ");
            String generation = scanner.nextLine().trim();

            productService.registerConsole(
                    id,
                    title,
                    price,
                    stock,
                    brand,
                    model,
                    generation
            );

            printMessage("Consola registrada correctamente.");

        } catch (NumberFormatException exception) {
            printError("El precio y el inventario deben ser numeros validos.");
        } catch (IllegalArgumentException exception) {
            printError(exception.getMessage());
        }
    }

    /**
     * Lists all registered products.
     */
    private void listAllProducts() {
        printHeader("LISTADO DE PRODUCTOS");

        List<Product> products = productService.listAllProducts();

        if (products.isEmpty()) {
            printMessage("Aun no hay productos registrados.");
            return;
        }

        for (Product product : products) {
            System.out.println(product.getDescription());
            printLine();
        }
    }

    /**
     * Registers a new customer.
     */
    private void registerCustomer() {
        printHeader("REGISTRAR CLIENTE");

        System.out.print("ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Nombre: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Apellido: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Telefono: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Correo electronico: ");
        String email = scanner.nextLine().trim();

        try {
            personService.registerCustomer(
                    id,
                    firstName,
                    lastName,
                    phone,
                    email
            );

            printMessage("Cliente registrado correctamente.");

        } catch (IllegalArgumentException exception) {
            printError(exception.getMessage());
        }
    }

    /**
     * Lists all registered customers.
     */
    private void listAllCustomers() {
        printHeader("LISTADO DE CLIENTES");

        List<Customer> customers = personService.listAllCustomers();

        if (customers.isEmpty()) {
            printMessage("Aun no hay clientes registrados.");
            return;
        }

        for (Customer customer : customers) {
            System.out.println("ID: " + customer.getId());
            System.out.println("Nombre: " + customer.getFullName());
            printLine();
        }
    }

    /**
     * Lists all registered sellers.
     */
    private void listAllSellers() {
        printHeader("LISTADO DE VENDEDORES");

        List<Seller> sellers = personService.listAllSellers();

        if (sellers.isEmpty()) {
            printMessage("No hay vendedores registrados.");
            return;
        }

        for (Seller seller : sellers) {
            System.out.println("ID: " + seller.getId());
            System.out.println("Nombre: " + seller.getFullName());
            printLine();
        }
    }

    /**
     * Registers a new sale.
     */
    private void registerSale() {
        printHeader("REGISTRAR VENTA");

        System.out.print("ID del cliente: ");
        String customerId = scanner.nextLine().trim();

        Customer customer = personService.findCustomerById(customerId);

        if (customer == null) {
            printError("Cliente no encontrado.");
            return;
        }

        System.out.print("ID del vendedor: ");
        String sellerId = scanner.nextLine().trim();

        Seller seller = personService.findSellerById(sellerId);

        if (seller == null) {
            printError("Vendedor no encontrado.");
            return;
        }

        List<Product> products = new ArrayList<>();

        System.out.println("Ingrese los IDs de productos uno por uno.");
        System.out.println("Escriba 'fin' cuando termine.");

        while (true) {
            System.out.print("ID de producto (o 'fin'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("fin")) {
                break;
            }

            Product product = productService.findById(input);

            if (product == null) {
                printError("Producto no encontrado. Intente nuevamente.");
                continue;
            }

            products.add(product);
            printMessage("Producto agregado: " + product.getDescription());
        }

        try {
            Sale sale = saleService.registerSale(customer, seller, products);
            printMessage("Venta registrada correctamente.");
            System.out.println();
            System.out.println(sale.generateReceipt());

        } catch (IllegalArgumentException exception) {
            printError("No se pudo registrar la venta: "
                    + exception.getMessage());
        }
    }

    /**
     * Lists all registered sales.
     */
    private void viewAllSales() {
        printHeader("HISTORIAL DE VENTAS");

        List<Sale> sales = saleService.viewAllSales();

        if (sales.isEmpty()) {
            printMessage("Aun no hay ventas registradas.");
            return;
        }

        for (Sale sale : sales) {
            System.out.println(sale.generateReceipt());
            printLine();
        }
    }

    /**
     * Lists sales made by a specific customer.
     */
    private void viewSalesByCustomer() {
        printHeader("VENTAS POR CLIENTE");

        System.out.print("ID del cliente: ");
        String customerId = scanner.nextLine().trim();

        Customer customer = personService.findCustomerById(customerId);

        if (customer == null) {
            printError("Cliente no encontrado.");
            return;
        }

        List<Sale> sales = saleService.viewSalesByCustomer(customer);

        if (sales.isEmpty()) {
            printMessage("Este cliente aun no tiene compras.");
            return;
        }

        for (Sale sale : sales) {
            System.out.println(sale.generateReceipt());
            printLine();
        }
    }

    /**
     * Lists sales attended by a specific seller.
     */
    private void viewSalesBySeller() {
        printHeader("VENTAS POR VENDEDOR");

        System.out.print("ID del vendedor: ");
        String sellerId = scanner.nextLine().trim();

        Seller seller = personService.findSellerById(sellerId);

        if (seller == null) {
            printError("Vendedor no encontrado.");
            return;
        }

        List<Sale> sales = saleService.viewSalesBySeller(seller);

        if (sales.isEmpty()) {
            printMessage("Este vendedor aun no tiene ventas.");
            return;
        }

        for (Sale sale : sales) {
            System.out.println(sale.generateReceipt());
            printLine();
        }
    }

    /**
     * Registers a percentage-based promotion.
     */
    private void registerPercentageDiscount() {
        try {
            printHeader("PROMOCION POR PORCENTAJE");

            System.out.print("ID de la promocion: ");
            String id = scanner.nextLine().trim();

            System.out.print("Nombre de la promocion: ");
            String name = scanner.nextLine().trim();

            LocalDate startDate = readDate(
                    "Fecha de inicio (AAAA-MM-DD): "
            );

            LocalDate endDate = readDate(
                    "Fecha de fin (AAAA-MM-DD): "
            );

            System.out.print("Porcentaje de descuento: ");
            double discountPercentage =
                    Double.parseDouble(scanner.nextLine().trim());

            promotionService.registerPercentageDiscount(
                    id,
                    name,
                    startDate,
                    endDate,
                    discountPercentage
            );

            printMessage("Promocion por porcentaje registrada correctamente.");

        } catch (NumberFormatException exception) {
            printError("El porcentaje debe ser un numero valido.");
        } catch (IllegalArgumentException exception) {
            printError(exception.getMessage());
        }
    }

    /**
     * Registers a category-based promotion.
     */
    private void registerCategoryDiscount() {
        try {
            printHeader("PROMOCION POR CATEGORIA");

            System.out.print("ID de la promocion: ");
            String id = scanner.nextLine().trim();

            System.out.print("Nombre de la promocion: ");
            String name = scanner.nextLine().trim();

            LocalDate startDate = readDate(
                    "Fecha de inicio (AAAA-MM-DD): "
            );

            LocalDate endDate = readDate(
                    "Fecha de fin (AAAA-MM-DD): "
            );

            System.out.print("Porcentaje de descuento: ");
            double discountPercentage =
                    Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Categoria (VIDEOGAME o CONSOLE): ");
            String categoryObjective =
                    scanner.nextLine().trim().toUpperCase();

            if (!categoryObjective.equals("VIDEOGAME")
                    && !categoryObjective.equals("CONSOLE")) {
                printError("La categoria debe ser VIDEOGAME o CONSOLE.");
                return;
            }

            promotionService.registerCategoryDiscount(
                    id,
                    name,
                    startDate,
                    endDate,
                    discountPercentage,
                    categoryObjective
            );

            printMessage("Promocion por categoria registrada correctamente.");

        } catch (NumberFormatException exception) {
            printError("El porcentaje debe ser un numero valido.");
        } catch (IllegalArgumentException exception) {
            printError(exception.getMessage());
        }
    }

    /**
     * Registers a bulk purchase promotion.
     */
    private void registerBulkPurchaseDiscount() {
        try {
            printHeader("PROMOCION POR VOLUMEN");

            System.out.print("ID de la promocion: ");
            String id = scanner.nextLine().trim();

            System.out.print("Nombre de la promocion: ");
            String name = scanner.nextLine().trim();

            LocalDate startDate = readDate(
                    "Fecha de inicio (AAAA-MM-DD): "
            );

            LocalDate endDate = readDate(
                    "Fecha de fin (AAAA-MM-DD): "
            );

            System.out.print("Cantidad minima de productos: ");
            int minimumQuantity =
                    Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Porcentaje de descuento: ");
            double discountPercentage =
                    Double.parseDouble(scanner.nextLine().trim());

            promotionService.registerBulkPurchaseDiscount(
                    id,
                    name,
                    startDate,
                    endDate,
                    minimumQuantity,
                    discountPercentage
            );

            printMessage("Promocion por volumen registrada correctamente.");

        } catch (NumberFormatException exception) {
            printError("La cantidad y el porcentaje deben ser numeros validos.");
        } catch (IllegalArgumentException exception) {
            printError(exception.getMessage());
        }
    }

    /**
     * Lists all registered promotions.
     */
    private void listAllPromotions() {
        printHeader("LISTADO DE PROMOCIONES");

        List<Promotion> promotions = promotionService.listAllPromotions();

        if (promotions.isEmpty()) {
            printMessage("No hay promociones registradas.");
            return;
        }

        for (Promotion promotion : promotions) {
            printPromotion(promotion);
        }
    }

    /**
     * Lists promotions active on the current date.
     */
    private void listActivePromotions() {
        printHeader("PROMOCIONES VIGENTES");

        List<Promotion> promotions = promotionService.listActivePromotions();

        if (promotions.isEmpty()) {
            printMessage("No hay promociones vigentes actualmente.");
            return;
        }

        for (Promotion promotion : promotions) {
            printPromotion(promotion);
        }
    }

    /**
     * Prints the basic information of a promotion.
     *
     * @param promotion the promotion to display
     */
    private void printPromotion(Promotion promotion) {
        System.out.println("ID: " + promotion.getId());
        System.out.println("Nombre: " + promotion.getName());
        System.out.println("Inicio: " + promotion.getStartDate());
        System.out.println("Fin: " + promotion.getEndDate());
        System.out.println("Tipo: "
                + promotion.getClass().getSimpleName());
        printLine();
    }

    /**
     * Reads a date in ISO format from the user.
     *
     * @param message the message displayed before reading the date
     * @return the valid date entered by the user
     */
    private LocalDate readDate(String message) {
        while (true) {
            try {
                System.out.print(message);
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException exception) {
                printError("Fecha invalida. Use el formato AAAA-MM-DD.");
            }
        }
    }

    /**
     * Prints a formatted menu header.
     *
     * @param title the title to display
     */
    private void printHeader(String title) {
        System.out.println();
        System.out.println("==============================================");
        System.out.println(" " + title);
        System.out.println("==============================================");
    }

    /**
     * Prints a visual separation line.
     */
    private void printLine() {
        System.out.println("----------------------------------------------");
    }

    /**
     * Prints a successful operation message.
     *
     * @param message the message to display
     */
    private void printMessage(String message) {
        System.out.println();
        System.out.println("OK: " + message);
    }

    /**
     * Prints an error message.
     *
     * @param message the error message to display
     */
    private void printError(String message) {
        System.out.println();
        System.out.println("ERROR: " + message);
    }

    /**
     * Waits until the user presses Enter.
     */
    private void pause() {
        System.out.println();
        System.out.print("Presione Enter para continuar...");
        scanner.nextLine();
    }
}