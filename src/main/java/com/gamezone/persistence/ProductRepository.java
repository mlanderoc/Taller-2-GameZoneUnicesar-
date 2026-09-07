
package com.gamezone.persistence;

import com.gamezone.model.Product;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;



public class ProductRepository {

    private static final String FILE_PATH = "data/products.dat";

    
    public void saveALL(List<Product> products){
        File dataDirectory = new File("data"); 
        
        if(!dataDirectory.exists()){
            dataDirectory.mkdir();
        }
        
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))){
            
               output.writeObject(products);
            
        } catch(IOException exception){
            throw new RuntimeException("Error saving products", exception);
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public List<Product> loadAll(){
        File dataFile = new File(FILE_PATH); 
        
        if(!dataFile.exists()){
            return new ArrayList<>(); 
        }
        
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(FILE_PATH))){
            
            return (List<Product>) input.readObject();
            
        } catch (IOException | ClassNotFoundException exception){
            throw new RuntimeException("Error loading products", exception);
        }
    }
}   
    
