
package com.gamezone.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the file-based persistence of return records. Works only with
 * ReturnRecord (raw ids), never with full domain objects, keeping this class
 * independent from the service layer.
 */
public class ReturnRepository implements Repository<ReturnRecord> {

    private static final String FILE_PATH = "data/returns.dat";

    @Override
    public void saveAll(List<ReturnRecord> records) {
        File dataDirectory = new File("data");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdir();
        }

        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {

            output.writeObject(records);

        } catch (IOException exception) {
            throw new RuntimeException("Error saving returns", exception);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ReturnRecord> loadAll() {
        File dataFile = new File(FILE_PATH);
        if (!dataFile.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {

            return (List<ReturnRecord>) input.readObject();

        } catch (IOException | ClassNotFoundException exception) {
            throw new RuntimeException("Error loading returns", exception);
        }
    }
}
