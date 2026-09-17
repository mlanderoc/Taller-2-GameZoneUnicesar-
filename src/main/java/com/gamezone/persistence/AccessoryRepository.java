
package com.gamezone.persistence;
import com.gamezone.model.Accessory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AccessoryRepository {
     private static final String FILE_PATH = "data/Accessory.dat";
     
     public void saveAll(List<Accessory> accessories){
        File dataDirectory = new File("data"); 
        
        if(!dataDirectory.exists()){
            dataDirectory.mkdir();
        }
        
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))){
            
               output.writeObject(accessories);
            
        } catch(IOException exception){
            throw new RuntimeException("Error saving Accesories", exception);
        }
    }
     
         @SuppressWarnings("unchecked")
    public List<Accessory> loadAll(){
        File dataFile = new File(FILE_PATH); 
        
        if(!dataFile.exists()){
            return new ArrayList<>(); 
        }
        
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(FILE_PATH))){
            
            return (List<Accessory>) input.readObject();
            
        } catch (IOException | ClassNotFoundException exception){
            throw new RuntimeException("Error loading Accesories", exception);
        }
    }
    
    
}
