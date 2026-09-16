
package com.gamezone.persistence;

import com.gamezone.model.Return;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnRepository {
 
      private static final String FILE_PATH = "data/return.dat";
      
      public void saveAll(List<Return> returns){
        File dataDirectory = new File("data"); 
        
        if(!dataDirectory.exists()){
            dataDirectory.mkdir();
        }
        
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))){
            
               output.writeObject(returns);
            
        } catch(IOException exception){
            throw new RuntimeException("Error saving returns", exception);
        }
        
    }
      @SuppressWarnings("unchecked")
    public List<Return> loadAll(){
        File dataFile = new File(FILE_PATH); 
        
        if(!dataFile.exists()){
            return new ArrayList<>(); 
        }
        
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(FILE_PATH))){
            
            return (List<Return>) input.readObject();
            
        } catch (IOException | ClassNotFoundException exception){
            throw new RuntimeException("Error loading Returns", exception);
        }
    }
}
