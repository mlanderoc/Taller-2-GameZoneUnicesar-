package com.gamezone;

import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.PromotionService;
import java.time.LocalDate;
import java.util.List;

/**
 * Dedicated startup data initializer responsible for seeding default
 * sellers, promotions, and accessories when persistent storage files
 * are empty. Extracted from {@link Main} to satisfy the Single
 * Responsibility Principle (SRP).
 */
public final class DataInitializer {

    private DataInitializer() {
    }

    /**
     * Seeds all required initial datasets (sellers, promotions, and accessories)
     * if their corresponding repositories are empty.
     *
     * @param personService    service managing sellers and customers
     * @param promotionService service managing promotional campaigns
     * @param accessoryService service managing accessories
     */
    public static void initializeAll(PersonService personService,
                                     PromotionService promotionService,
                                     AccessoryService accessoryService) {
        preloadSellersIfNeeded(personService);
        preloadPromotionsIfNeeded(promotionService);
        preloadAccessoriesIfNeeded(accessoryService);
    }

    /**
     * Ensures at least three sellers exist on first run, since sellers
     * are already hired staff and are not registered through the UI.
     *
     * @param personService service managing sellers
     */
    public static void preloadSellersIfNeeded(PersonService personService) {
        if (personService.listAllSellers().isEmpty()) {
            personService.registerSeller("V001", "Laura", "Gómez", "3001234567", "EMP001", "Morning");
            personService.registerSeller("V002", "Carlos", "Pérez", "3007654321", "EMP002", "Afternoon");
            personService.registerSeller("V003", "Ana", "Rodríguez", "3009876543", "EMP003", "Evening");
            System.out.println("Sellers preloaded successfully.");
        }
    }

    /**
     * Ensures the four default promotions exist on first run.
     *
     * @param promotionService service managing promotions
     */
    public static void preloadPromotionsIfNeeded(PromotionService promotionService) {
        if (promotionService.listAllPromotions().isEmpty()) {
            promotionService.registerPercentageDiscount(
                    "PROMO001", "Descuento general", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), 15.0);
            promotionService.registerCategoryDiscount(
                    "PROMO002", "Descuento videojuegos", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), 20.0, "VIDEOGAME");
            promotionService.registerBulkPurchaseDiscount(
                    "PROMO003", "Descuento por volumen", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), 3, 10.0);
            promotionService.registerCategoryDiscount(
                    "PROMO004", "Descuento accesorios", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), 25.0, "ACCESSORY");
            System.out.println("Promotions preloaded successfully.");
        } else if (promotionService.findById("PROMO004") == null) {
            promotionService.registerCategoryDiscount(
                    "PROMO004", "Descuento accesorios", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 12, 31), 25.0, "ACCESSORY");
        }
    }

    /**
     * Ensures the three default accessories exist on first run.
     *
     * @param accessoryService service managing accessories
     */
    public static void preloadAccessoriesIfNeeded(AccessoryService accessoryService) {
        if (accessoryService.listAllaccessories().isEmpty()) {
            accessoryService.registerController(
                    "WIRELESS", List.of("C001", "C002"),
                    "A001", "Control inalambrico", 150000.0, 10);

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
