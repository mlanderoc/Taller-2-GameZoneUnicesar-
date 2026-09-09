package com.gamezone;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.service.PromotionService;
import com.gamezone.ui.ConsoleMenu;

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

        ProductService productService = new ProductService(productRepository);
        PersonService personService = new PersonService(personRepository);
        SaleService saleService = new SaleService(saleRepository, productService, personService);
        PromotionService promotionService = new PromotionService(promotionRepository);

        preloadSellersIfNeeded(personService);

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
}