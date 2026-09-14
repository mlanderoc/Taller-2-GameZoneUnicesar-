package com.gamezone.persistence;


import com.gamezone.model.Promotion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PromotionRepository implements Repository<Promotion>  {

    private static final String FILE_PATH = "data/promotion.dat";

    @Override
    public void saveAll(List<Promotion> promotions) {
        File dataDirectory = new File("data");

        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }

        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {

            output.writeObject(promotions);

        } catch (IOException exception) {
            throw new RuntimeException("Error saving promotions", exception);
        }
    }
      @SuppressWarnings("unchecked")
    public List<Promotion> loadAll(){
        File dataFile = new File(FILE_PATH); 
        
        if(!dataFile.exists()){
            return new ArrayList<>(); 
        }
        
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(FILE_PATH))){
            
            return (List<Promotion>) input.readObject();
            
        } catch (IOException | ClassNotFoundException exception){
            throw new RuntimeException("Error loading promotions", exception);
        }
    }
}