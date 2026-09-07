
package com.gamezone.service;

import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.model.Console;
import com.gamezone.persistence.ProductRepository;
import java.util.List;

public class ProductService {
    
    private final ProductRepository repository;
    private final List<Product> products;

    public ProductService(ProductRepository repository) {
    this.repository = repository;
    this.products = repository.loadAll();
    }
    
    
    public Product findById(String id){
        for (Product product : products){
            if (product.getId().equals(id)){
                return product;
            }
        }
        
        return null; 
    }
    
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
    
    public List<Product> listAllProducts(){
        return products; 
    }
    
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
