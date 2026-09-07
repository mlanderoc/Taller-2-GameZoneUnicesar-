
package com.gamezone.persistence;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SaleRepository implements Repository<SaleRecord>{
    private static final String FILE_PATH = "data/sales.dat";
    
    public void saveAll(List<SaleRecord> records) {
        File dataDirectory = new File("data");

        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }

        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {

            output.writeObject(records);

        } catch (IOException exception) {
            throw new RuntimeException("Error saving sale records", exception);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SaleRecord> loadAll() {
        File dataFile = new File(FILE_PATH);

        if (!dataFile.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {

            return (List<SaleRecord>) input.readObject();

        } catch (IOException | ClassNotFoundException exception) {
            throw new RuntimeException("Error loading sale records", exception);
        }
    }
}
