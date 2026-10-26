package com.gamezone;

import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.persistence.ReturnRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;

/**
 * Entry point of the GameZone Unicesar application.
 * Wires together the persistence, service, and UI layers, and
 * delegates initial dataset seeding to {@link DataInitializer} (SRP).
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
        AccessoryService accessoryService = new AccessoryService(accessoryRepository);

        WarrantyService warrantyService = new WarrantyService(warrantyRepository, saleRepository, productService);
        SaleService saleService = new SaleService(saleRepository, productService, personService, promotionService, accessoryService, warrantyService);
        ReturnService returnService = new ReturnService(returnRepository, saleService, productService, accessoryService, warrantyService);

        DataInitializer.initializeAll(personService, promotionService, accessoryService);

        java.awt.EventQueue.invokeLater(() -> {
            new com.gamezone.ui.MainFrame(productService, personService, saleService, accessoryService, promotionService, returnService, warrantyService).setVisible(true);
        });
    }

    /**
     * Delegates seller seeding to {@link DataInitializer#preloadSellersIfNeeded(PersonService)}.
     */
    public static void preloadSellersIfNeeded(PersonService personService) {
        DataInitializer.preloadSellersIfNeeded(personService);
    }

    /**
     * Delegates promotion seeding to {@link DataInitializer#preloadPromotionsIfNeeded(PromotionService)}.
     */
    public static void preloadPromotionsIfNeeded(PromotionService promotionService) {
        DataInitializer.preloadPromotionsIfNeeded(promotionService);
    }

    /**
     * Delegates accessory seeding to {@link DataInitializer#preloadAccessoriesIfNeeded(AccessoryService)}.
     */
    public static void preloadAccessoriesIfNeeded(AccessoryService accessoryService) {
        DataInitializer.preloadAccessoriesIfNeeded(accessoryService);
    }
}
