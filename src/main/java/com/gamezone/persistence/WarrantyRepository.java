
package com.gamezone.persistence;
import com.gamezone.model.Warranty;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class WarrantyRepository {
      private static final String FILE_PATH = "data/warranties.dat";
      
      public void saveAll(List<Warranty> warranties){
        File dataDirectory = new File("data"); 
        
        if(!dataDirectory.exists()){
            dataDirectory.mkdir();
        }
        
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))){
            
               output.writeObject(warranties);
            
        } catch(IOException exception){
            throw new RuntimeException("Error saving warranties", exception);
        }
    }
          @SuppressWarnings("unchecked")
    public List<Warranty> loadAll(){
        File dataFile = new File(FILE_PATH); 
        
        if(!dataFile.exists()){
            return new ArrayList<>(); 
        }
        
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(FILE_PATH))){
            
            return (List<Warranty>) input.readObject();
            
        } catch (IOException | ClassNotFoundException exception){
            throw new RuntimeException("Error loading warranties", exception);
        }
    }
}
