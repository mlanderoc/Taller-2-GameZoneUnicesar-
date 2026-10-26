package com.gamezone.persistence;

import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.Accessory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the file-based persistence of accessories, implementing the
 * {@link Repository} abstraction for Dependency Inversion (DIP).
 */
public class AccessoryRepository implements Repository<Accessory> {

    private static final String FILE_PATH = "data/Accessory.dat";

    @Override
    public void saveAll(List<Accessory> accessories) {
        File dataDirectory = new File("data");

        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }

        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {

            output.writeObject(accessories);

        } catch (IOException exception) {
            throw new PersistenceException("Error de escritura en el archivo " + FILE_PATH, exception);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Accessory> loadAll() {
        File dataFile = new File(FILE_PATH);

        if (!dataFile.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {

            return (List<Accessory>) input.readObject();

        } catch (IOException | ClassNotFoundException exception) {
            throw new PersistenceException("Error de lectura en el archivo " + FILE_PATH, exception);
        }
    }
}
