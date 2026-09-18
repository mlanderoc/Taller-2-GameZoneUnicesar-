package com.gamezone.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the file-based persistence of warranty records.
 * Works only with WarrantyRecord (raw ids), never with full domain objects,
 * keeping this class independent from the service layer.
 */
public class WarrantyRepository implements Repository<WarrantyRecord> {

    private static final String FILE_PATH = "data/warranties.dat";

    @Override
    public void saveAll(List<WarrantyRecord> records) {
        File dataDirectory = new File("data");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }

        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {

            output.writeObject(records);

        } catch (IOException exception) {
            throw new RuntimeException("Error saving warranties", exception);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<WarrantyRecord> loadAll() {
        File dataFile = new File(FILE_PATH);
        if (!dataFile.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {

            return (List<WarrantyRecord>) input.readObject();

        } catch (IOException | ClassNotFoundException exception) {
            throw new RuntimeException("Error loading warranties", exception);
        }
    }
}