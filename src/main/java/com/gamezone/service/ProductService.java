
package com.gamezone.service;

import gamezone.model.Product;
import gamezone.model.VideoGame;
import gamezone.model.Console;
import gamezone.persistence.ProductRepository;
import java.util.List;

/**
 * Manages business operations related to products.
 */
public class ProductService {
    
    private final ProductRepository repository;
    private final List<Product> products;
    
    /**
     * Creates a product service using the given repository.
     *
     * @param repository the repository used to load and save products
     */
    public ProductService(ProductRepository repository) {
    this.repository = repository;
    this.products = repository.loadAll();
    }
    
    /**
 * Finds a product by its identifier.
 *
 * @param id the identifier of the product to find
 * @return the matching product, or null if no product is found
 */
    public Product findById(String id){
        for (Product product : products){
            if (product.getId().equals(id)){
                return product;
            }
        }
        
        return null; 
    }
    
    /**
     * Registers a new video game.
     *
     * @param id the unique identifier of the video game
     * @param title the title of the video game
     * @param price the price of the video game
     * @param stock the available quantity in inventory
     * @param platform the platform on which the video game is played
     * @param genre the genre of the video game
     * @param ageRating the recommended age rating of the video game
     */
    public void registerVideoGame(String id, String title, double price, int stock,
                String platform, String genre, String ageRating) {
        
        if(findById(id) != null){
            throw new IllegalArgumentException ("A product with this ID already exists");
        }
        
        VideoGame videoGame = new VideoGame(
                id, title,
                price, stock,
                platform, genre,
                ageRating
        );
        
        products.add(videoGame); 
        repository.saveAll(products);
    }
    
    /**
     * Registers a new console.
     *
     * @param id the unique identifier of the console
     * @param title the title of the console
     * @param price the price of the console
     * @param stock the available quantity in inventory
     * @param brand the brand of the console
     * @param model the model of the console
     * @param generation the generation of the console
     */
    public void registerConsole(String id, String title, double price, int stock,
                String brand, String model, String generation){
      
        if(findById(id) != null){
            throw new IllegalArgumentException(
                    "A product with this ID already exists"
            );
        }
        
        Console console = new Console(
                id, title,
                price, stock,
                brand, model,
                generation  
        );
        
        products.add(console); 
        repository.saveAll(products);
    }
    
    /**
     * Returns all registered products.
     *
     * @return a list containing all products
     */
    public List<Product> listAllProducts(){
        return products; 
    }
    
    /**
     * Updates the stock of a product.
     *
     * @param productId the identifier of the product to update
     * @param quantity the quantity to add to or subtract from the stock
     */
    public void updateStock(String productId, int quantity){
        Product product = findById(productId);
        
        if(product == null){
            throw new IllegalArgumentException("Product not found");
        }
        
        if(product.getStock() + quantity < 0){
            throw new IllegalArgumentException(
                    "the stock cannot be negative"
            ); 
        }
        product.updateStock(quantity);
        repository.saveAll(products);
    }
}
