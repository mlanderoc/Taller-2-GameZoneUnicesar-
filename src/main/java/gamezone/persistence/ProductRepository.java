
package gamezone.persistence;

import gamezone.model.Product;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;



/**
 * Manages the file-based persistence of products.
 */
public class ProductRepository {

    private static final String FILE_PATH = "data/products.dat";

    /**
     * Saves all products to the data file.
     *
     * @param products the list of products to save
     */
    public void saveAll(List<Product> products){
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
    
     /**
     * Loads all products from the data file.
     *
     * @return a list containing all loaded products
     */
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
    
