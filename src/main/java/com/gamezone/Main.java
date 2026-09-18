package com.gamezone;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.WarrantyService;
import com.gamezone.ui.ConsoleMenu;
import java.time.LocalDate;
import java.util.List;

/**
 * Entry point of the GameZone Unicesar application.
 * Wires together the persistence, service, and UI layers, and
 * ensures that seller data is preloaded before the menu starts.
 */
public class Main {

    public static void main(String[] args) {
        ProductRepository productRepository = new ProductRepository();
        PersonRepository personRepository = new PersonRepository();
        SaleRepository saleRepository = new SaleRepository();
        PromotionRepository promotionRepository = new PromotionRepository();
        AccessoryRepository accessoryRepository = new AccessoryRepository();
        ReturnRepository returnRepository = new ReturnRepository();
        WarrantyRepository warrantyRepository = new WarrantyRepository();
        
        ProductService productService = new ProductService(productRepository);
        PersonService personService = new PersonService(personRepository);
        PromotionService promotionService = new PromotionService(promotionRepository);
        AccessoryService accessoryService = new AccessoryService(accessoryRepository,accessoryRepository.loadAll());
        
        SaleService saleService = new SaleService(saleRepository, productService, personService,promotionService,accessoryService);
        WarrantyService warrantyService = new WarrantyService(warrantyRepository, saleService, productService);
        saleService.setWarrantyService(warrantyService);
        ReturnService returnService = new ReturnService(returnRepository, saleService, productService, accessoryService);

        preloadSellersIfNeeded(personService);
        preloadPromotionsIfNeeded(promotionService);
        preloadAccessoriesIfNeeded(accessoryService);

        ConsoleMenu menu = new ConsoleMenu(productService, personService, saleService, promotionService);
        menu.start();
    }

    /**
     * Ensures at least three sellers exist on first run, since sellers
     * are already hired staff and are not registered through the UI.
     */
    private static void preloadSellersIfNeeded(PersonService personService) {
        if (personService.listAllSellers().isEmpty()) {
            personService.registerSeller("V001", "Laura", "Gómez", "3001234567", "EMP001", "Morning");
            personService.registerSeller("V002", "Carlos", "Pérez", "3007654321", "EMP002", "Afternoon");
            personService.registerSeller("V003", "Ana", "Rodríguez", "3009876543", "EMP003", "Evening");
            System.out.println("Sellers preloaded successfully.");
        }
    }
    //add preload promotions
    private static void preloadPromotionsIfNeeded(PromotionService promotionService) {
        if (promotionService.listAllPromotions().isEmpty()) {
            promotionService.registerPercentageDiscount(
                    "PROMO001", "Descuento general", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30), 15.0);
            promotionService.registerCategoryDiscount(
                    "PROMO002", "Descuento videojuegos", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30), 20.0, "VIDEOGAME");
            promotionService.registerBulkPurchaseDiscount(
                    "PROMO003", "Descuento por volumen", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 30), 3, 10.0);
            System.out.println("Promotions preloaded successfully.");
        }
    }
    //add preload accessory
    private static void preloadAccessoriesIfNeeded(AccessoryService accessoryService) {
    if (accessoryService.listAllaccessories().isEmpty()) {
        accessoryService.registerController(
                "WIRELESS", List.of("C001", "C002"),
                "A001", "Control inalámbrico", 150000.0, 10);

        accessoryService.registerCable(
                2.0, "HDMI", List.of("C001", "C002"),
                "A002", "Cable HDMI 2m", 30000.0, 20);

        accessoryService.registerMemory(
                8, "SD", List.of("C001"),
                "A003", "Memoria SD 8GB", 25000.0, 15);

        System.out.println("Accesorios precargados exitosamente.");
    }
}
}