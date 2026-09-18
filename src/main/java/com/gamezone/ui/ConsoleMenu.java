package com.gamezone.ui;

import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.model.Warranty;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for GameZone.
 */
public class ConsoleMenu {

    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;
    private final AccessoryService accessoryService;
    private final PromotionService promotionService;
    private final ReturnService returnService;
    private final WarrantyService warrantyService;
    private final Scanner scanner;

    public ConsoleMenu(ProductService productService,
                       PersonService personService,
                       SaleService saleService,
                       AccessoryService accessoryService,
                       PromotionService promotionService,
                       ReturnService returnService,
                       WarrantyService warrantyService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.returnService = returnService;
        this.warrantyService = warrantyService;
        this.scanner = new Scanner(System.in);
    }

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
                case "5":
                    showAccessoryMenu();
                    break;
                case "6":
                    showReturnMenu();
                    break;
                case "7":
                    showWarrantyMenu();
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
    }

    private void printMainMenu() {
        printHeader("GAMEZONE UNICESAR");
        System.out.println("       Sistema de gestion de videojuegos");
        printLine();
        System.out.println("  [1] Gestion de productos");
        System.out.println("  [2] Gestion de personas");
        System.out.println("  [3] Gestion de ventas");
        System.out.println("  [4] Gestion de promociones");
        System.out.println("  [5] Gestion de accesorios");
        System.out.println("  [6] Gestion de devoluciones");
        System.out.println("  [7] Gestion de garantias");
        System.out.println("  [0] Salir");
        printLine();
        System.out.print("Seleccione una opcion: ");
    }

    // ===== PRODUCT MENU =====
    private void showProductMenu() {
        boolean inMenu = true;
        while (inMenu) {
            printHeader("GESTION DE PRODUCTOS");
            System.out.println("  [1] Registrar videojuego");
            System.out.println("  [2] Registrar consola");
            System.out.println("  [3] Listar todos los productos");
            System.out.println("  [0] Volver al menu principal");
            printLine();
            System.out.print("Seleccione una opcion: ");

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1": registerVideoGame(); pause(); break;
                case "2": registerConsole(); pause(); break;
                case "3": listAllProducts(); pause(); break;
                case "0": inMenu = false; break;
                default: printError("Opcion invalida."); pause(); break;
            }
        }
    }

    // ===== PEOPLE MENU =====
    private void showPeopleMenu() {
        boolean inMenu = true;
        while (inMenu) {
            printHeader("GESTION DE PERSONAS");
            System.out.println("  [1] Registrar cliente");
            System.out.println("  [2] Listar clientes");
            System.out.println("  [3] Listar vendedores");
            System.out.println("  [0] Volver al menu principal");
            printLine();
            System.out.print("Seleccione una opcion: ");

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1": registerCustomer(); pause(); break;
                case "2": listAllCustomers(); pause(); break;
                case "3": listAllSellers(); pause(); break;
                case "0": inMenu = false; break;
                default: printError("Opcion invalida."); pause(); break;
            }
        }
    }

    // ===== SALES MENU =====
    private void showSalesMenu() {
        boolean inMenu = true;
        while (inMenu) {
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
                case "1": registerSale(); pause(); break;
                case "2": viewAllSales(); pause(); break;
                case "3": viewSalesByCustomer(); pause(); break;
                case "4": viewSalesBySeller(); pause(); break;
                case "0": inMenu = false; break;
                default: printError("Opcion invalida."); pause(); break;
            }
        }
    }

    // ===== PROMOTION MENU =====
    private void showPromotionMenu() {
        boolean inMenu = true;
        while (inMenu) {
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
                case "1": registerPercentageDiscount(); pause(); break;
                case "2": registerCategoryDiscount(); pause(); break;
                case "3": registerBulkPurchaseDiscount(); pause(); break;
                case "4": listAllPromotions(); pause(); break;
                case "5": listActivePromotions(); pause(); break;
                case "0": inMenu = false; break;
                default: printError("Opcion invalida."); pause(); break;
            }
        }
    }

    // ===== ACCESSORY MENU =====
    private void showAccessoryMenu() {
        boolean inMenu = true;
        while (inMenu) {
            printHeader("GESTION DE ACCESORIOS");
            System.out.println("  [1] Listar todos los accesorios");
            System.out.println("  [2] Buscar accesorios compatibles con consola");
            System.out.println("  [0] Volver al menu principal");
            printLine();
            System.out.print("Seleccione una opcion: ");

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1":
                    List<Accessory> accessories = accessoryService.listAllaccessories();
                    if (accessories.isEmpty()) printMessage("No hay accesorios registrados.");
                    else accessories.forEach(a -> System.out.println(a.getDescription()));
                    pause();
                    break;
                case "2":
                    System.out.print("ID de la consola: ");
                    String consoleId = scanner.nextLine().trim();
                    List<Accessory> compatible = accessoryService.findAcessoriesCOmpatibleWith(consoleId);
                    if (compatible.isEmpty()) printMessage("No hay accesorios compatibles para esta consola.");
                    else compatible.forEach(a -> System.out.println(a.getDescription()));
                    pause();
                    break;
                case "0": inMenu = false; break;
                default: printError("Opcion invalida."); pause(); break;
            }
        }
    }

    // ===== RETURN MENU =====
    private void showReturnMenu() {
        boolean inMenu = true;
        while (inMenu) {
            printHeader("GESTION DE DEVOLUCIONES");
            System.out.println("  [1] Registrar devolucion");
            System.out.println("  [2] Ver todas las devoluciones");
            System.out.println("  [3] Ver devoluciones por cliente");
            System.out.println("  [4] Ver devoluciones por venta");
            System.out.println("  [5] Generar balance mensual");
            System.out.println("  [0] Volver al menu principal");
            printLine();
            System.out.print("Seleccione una opcion: ");

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1": registerReturn(); pause(); break;
                case "2":
                    List<Return> returns = returnService.viewAllReturns();
                    if (returns.isEmpty()) printMessage("No hay devoluciones registradas.");
                    else returns.forEach(r -> System.out.println(r.generateReturnReceipt()));
                    pause();
                    break;
                case "3":
                    System.out.print("ID del cliente: ");
                    String cId = scanner.nextLine().trim();
                    List<Return> cReturns = returnService.viewReturnsByCustomer(cId);
                    if (cReturns.isEmpty()) printMessage("No hay devoluciones para este cliente.");
                    else cReturns.forEach(r -> System.out.println(r.generateReturnReceipt()));
                    pause();
                    break;
                case "4":
                    System.out.print("ID de la venta: ");
                    String sId = scanner.nextLine().trim();
                    List<Return> sReturns = returnService.viewReturnsBySale(sId);
                    if (sReturns.isEmpty()) printMessage("No hay devoluciones para esta venta.");
                    else sReturns.forEach(r -> System.out.println(r.generateReturnReceipt()));
                    pause();
                    break;
                case "5":
                    try {
                        System.out.print("Mes (1-12): ");
                        int month = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Año (ej. 2026): ");
                        int year = Integer.parseInt(scanner.nextLine().trim());
                        double balance = returnService.generateMonthlyBalance(month, year);
                        printMessage("Balance neto para " + month + "/" + year + ": $" + balance);
                    } catch (NumberFormatException e) {
                        printError("Mes y año deben ser valores numericos.");
                    }
                    pause();
                    break;
                case "0": inMenu = false; break;
                default: printError("Opcion invalida."); pause(); break;
            }
        }
    }

    // ===== WARRANTY MENU =====
    private void showWarrantyMenu() {
        boolean inMenu = true;
        while (inMenu) {
            printHeader("GESTION DE GARANTIAS");
            System.out.println("  [1] Listar todas las garantias");
            System.out.println("  [2] Listar garantias activas");
            System.out.println("  [3] Listar garantias por vencer");
            System.out.println("  [0] Volver al menu principal");
            printLine();
            System.out.print("Seleccione una opcion: ");

            String option = scanner.nextLine().trim();
            switch (option) {
                case "1":
                    List<Warranty> allW = warrantyService.listAllWarranties();
                    if (allW.isEmpty()) printMessage("No hay garantias registradas.");
                    else allW.forEach(w -> System.out.println(w.generateWarrantyCertificate()));
                    pause();
                    break;
                case "2":
                    List<Warranty> activeW = warrantyService.listActiveWarranties();
                    if (activeW.isEmpty()) printMessage("No hay garantias activas actualmente.");
                    else activeW.forEach(w -> System.out.println(w.generateWarrantyCertificate()));
                    pause();
                    break;
                case "3":
                    try {
                        System.out.print("Dias a verificar: ");
                        int days = Integer.parseInt(scanner.nextLine().trim());
                        List<Warranty> expiringW = warrantyService.listWarrantiesExpiringSoon(days);
                        if (expiringW.isEmpty()) printMessage("No hay garantias por vencer en ese periodo.");
                        else expiringW.forEach(w -> System.out.println(w.generateWarrantyCertificate()));
                    } catch (NumberFormatException e) {
                        printError("Los dias deben ser un valor numerico.");
                    }
                    pause();
                    break;
                case "0": inMenu = false; break;
                default: printError("Opcion invalida."); pause(); break;
            }
        }
    }

    // ===== LOGIC HELPERS =====
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
        List<String> productIdsWithExtendedWarranty = new ArrayList<>();

        System.out.println("Ingrese los IDs de productos uno por uno (escriba 'fin' para terminar):");
        while (true) {
            System.out.print("ID de producto/accesorio (o 'fin'): ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("fin")) break;

            Product product = productService.findById(input);
            if (product == null) {
                product = accessoryService.findById(input);
            }

            if (product == null) {
                printError("Producto no encontrado. Intente nuevamente.");
                continue;
            }

            products.add(product);
            printMessage("Agregado: " + product.getDescription());

            if (product instanceof Console) {
                System.out.print("¿Desea agregar garantia extendida a esta consola? (s/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                    productIdsWithExtendedWarranty.add(product.getId());
                }
            }
        }

        try {
            Sale sale = saleService.registerSale(customer, seller, products, productIdsWithExtendedWarranty);
            printMessage("Venta registrada correctamente.");
            System.out.println("\n" + sale.generateReceipt());
        } catch (IllegalArgumentException exception) {
            printError("No se pudo registrar la venta: " + exception.getMessage());
        }
    }

    private void registerReturn() {
        printHeader("REGISTRAR DEVOLUCION");

        System.out.print("ID de la venta original: ");
        String saleId = scanner.nextLine().trim();

        List<String> productIds = new ArrayList<>();
        System.out.println("Ingrese los IDs de productos a devolver (escriba 'fin' para terminar):");
        while (true) {
            System.out.print("ID de producto a devolver: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("fin")) break;
            productIds.add(input);
        }

        System.out.print("Motivo de la devolucion: ");
        String reason = scanner.nextLine().trim();

        try {
            Return returnObj = returnService.registerReturn(saleId, productIds, reason);
            printMessage("Devolucion registrada correctamente.");
            System.out.println("\n" + returnObj.generateReturnReceipt());
        } catch (IllegalArgumentException e) {
            printError("No se pudo procesar la devolucion: " + e.getMessage());
        }
    }

    private void registerVideoGame() {
        try {
            printHeader("REGISTRAR VIDEOJUEGO");
            System.out.print("ID: "); String id = scanner.nextLine().trim();
            System.out.print("Titulo: "); String title = scanner.nextLine().trim();
            System.out.print("Precio: "); double price = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Inventario: "); int stock = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Plataforma: "); String platform = scanner.nextLine().trim();
            System.out.print("Genero: "); String genre = scanner.nextLine().trim();
            System.out.print("Clasificacion: "); String ageRating = scanner.nextLine().trim();

            productService.registerVideoGame(id, title, price, stock, platform, genre, ageRating);
            printMessage("Videojuego registrado correctamente.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void registerConsole() {
        try {
            printHeader("REGISTRAR CONSOLA");
            System.out.print("ID: "); String id = scanner.nextLine().trim();
            System.out.print("Titulo: "); String title = scanner.nextLine().trim();
            System.out.print("Precio: "); double price = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Inventario: "); int stock = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Marca: "); String brand = scanner.nextLine().trim();
            System.out.print("Modelo: "); String model = scanner.nextLine().trim();
            System.out.print("Generacion: "); String generation = scanner.nextLine().trim();

            productService.registerConsole(id, title, price, stock, brand, model, generation);
            printMessage("Consola registrada correctamente.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void listAllProducts() {
        printHeader("LISTADO DE PRODUCTOS");
        List<Product> products = productService.listAllProducts();
        if (products.isEmpty()) printMessage("Aun no hay productos registrados.");
        else products.forEach(p -> System.out.println(p.getDescription()));
    }

    private void registerCustomer() {
        try {
            printHeader("REGISTRAR CLIENTE");
            System.out.print("ID: "); String id = scanner.nextLine().trim();
            System.out.print("Nombre: "); String firstName = scanner.nextLine().trim();
            System.out.print("Apellido: "); String lastName = scanner.nextLine().trim();
            System.out.print("Telefono: "); String phone = scanner.nextLine().trim();
            System.out.print("Correo: "); String email = scanner.nextLine().trim();

            personService.registerCustomer(id, firstName, lastName, phone, email);
            printMessage("Cliente registrado correctamente.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void listAllCustomers() {
        printHeader("LISTADO DE CLIENTES");
        List<Customer> customers = personService.listAllCustomers();
        if (customers.isEmpty()) printMessage("Aun no hay clientes registrados.");
        else customers.forEach(c -> System.out.println("ID: " + c.getId() + " - " + c.getFullName()));
    }

    private void listAllSellers() {
        printHeader("LISTADO DE VENDEDORES");
        List<Seller> sellers = personService.listAllSellers();
        if (sellers.isEmpty()) printMessage("No hay vendedores registrados.");
        else sellers.forEach(s -> System.out.println("ID: " + s.getId() + " - " + s.getFullName()));
    }

    private void viewAllSales() {
        printHeader("HISTORIAL DE VENTAS");
        List<Sale> sales = saleService.viewAllSales();
        if (sales.isEmpty()) printMessage("Aun no hay ventas registradas.");
        else sales.forEach(s -> System.out.println(s.generateReceipt() + "\n----------------------------------------------"));
    }

    private void viewSalesByCustomer() {
        System.out.print("ID del cliente: ");
        Customer c = personService.findCustomerById(scanner.nextLine().trim());
        if (c == null) { printError("Cliente no encontrado."); return; }
        List<Sale> sales = saleService.viewSalesByCustomer(c);
        if (sales.isEmpty()) printMessage("Este cliente no tiene compras.");
        else sales.forEach(s -> System.out.println(s.generateReceipt() + "\n----------------------------------------------"));
    }

    private void viewSalesBySeller() {
        System.out.print("ID del vendedor: ");
        Seller s = personService.findSellerById(scanner.nextLine().trim());
        if (s == null) { printError("Vendedor no encontrado."); return; }
        List<Sale> sales = saleService.viewSalesBySeller(s);
        if (sales.isEmpty()) printMessage("Este vendedor no tiene ventas.");
        else sales.forEach(sl -> System.out.println(sl.generateReceipt() + "\n----------------------------------------------"));
    }

    private void registerPercentageDiscount() {
        try {
            printHeader("PROMOCION POR PORCENTAJE");
            System.out.print("ID: "); String id = scanner.nextLine().trim();
            System.out.print("Nombre: "); String name = scanner.nextLine().trim();
            LocalDate start = readDate("Fecha inicio (AAAA-MM-DD): ");
            LocalDate end = readDate("Fecha fin (AAAA-MM-DD): ");
            System.out.print("Porcentaje: "); double pct = Double.parseDouble(scanner.nextLine().trim());

            promotionService.registerPercentageDiscount(id, name, start, end, pct);
            printMessage("Promocion registrada.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void registerCategoryDiscount() {
        try {
            printHeader("PROMOCION POR CATEGORIA");
            System.out.print("ID: "); String id = scanner.nextLine().trim();
            System.out.print("Nombre: "); String name = scanner.nextLine().trim();
            LocalDate start = readDate("Fecha inicio (AAAA-MM-DD): ");
            LocalDate end = readDate("Fecha fin (AAAA-MM-DD): ");
            System.out.print("Porcentaje: "); double pct = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Categoria (VIDEOGAME o CONSOLE): "); String cat = scanner.nextLine().trim().toUpperCase();

            promotionService.registerCategoryDiscount(id, name, start, end, pct, cat);
            printMessage("Promocion registrada.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void registerBulkPurchaseDiscount() {
        try {
            printHeader("PROMOCION POR VOLUMEN");
            System.out.print("ID: "); String id = scanner.nextLine().trim();
            System.out.print("Nombre: "); String name = scanner.nextLine().trim();
            LocalDate start = readDate("Fecha inicio (AAAA-MM-DD): ");
            LocalDate end = readDate("Fecha fin (AAAA-MM-DD): ");
            System.out.print("Cantidad minima: "); int min = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Porcentaje: "); double pct = Double.parseDouble(scanner.nextLine().trim());

            promotionService.registerBulkPurchaseDiscount(id, name, start, end, min, pct);
            printMessage("Promocion registrada.");
        } catch (Exception e) {
            printError(e.getMessage());
        }
    }

    private void listAllPromotions() {
        printHeader("LISTADO DE PROMOCIONES");
        List<Promotion> promotions = promotionService.listAllPromotions();
        if (promotions.isEmpty()) printMessage("No hay promociones registradas.");
        else promotions.forEach(this::printPromotion);
    }

    private void listActivePromotions() {
        printHeader("PROMOCIONES VIGENTES");
        List<Promotion> promotions = promotionService.listActivePromotions();
        if (promotions.isEmpty()) printMessage("No hay promociones vigentes.");
        else promotions.forEach(this::printPromotion);
    }

    private void printPromotion(Promotion promotion) {
        System.out.println("ID: " + promotion.getId() + " | Nombre: " + promotion.getName() + " | Tipo: " + promotion.getClass().getSimpleName());
        printLine();
    }

    private LocalDate readDate(String message) {
        while (true) {
            try {
                System.out.print(message);
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                printError("Fecha invalida. Use el formato AAAA-MM-DD.");
            }
        }
    }

    private void printHeader(String title) {
        System.out.println("\n==============================================");
        System.out.println(" " + title);
        System.out.println("==============================================");
    }

    private void printLine() {
        System.out.println("----------------------------------------------");
    }

    private void printMessage(String message) {
        System.out.println("\nOK: " + message);
    }

    private void printError(String message) {
        System.out.println("\nERROR: " + message);
    }

    private void pause() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}