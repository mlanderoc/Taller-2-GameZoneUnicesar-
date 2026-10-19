package com.gamezone.service;

import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Memory;
import com.gamezone.model.Controller;
import com.gamezone.validation.ProductValidator;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

public class AccessoryService {

    private final AccessoryRepository repository;
    private final List<Accessory> accessories;

    public AccessoryService(AccessoryRepository repository, List<Accessory> accessories) {
        this.repository = repository;
        this.accessories = repository.loadAll();
    }
    
    

    public Accessory findById(String id) {
        for (Accessory accessory : accessories) {
            if (accessory.getId().equals(id)) {
                return accessory;
            }
        }
        return null;
    }

    public void registerCable(double lengthInMeters, String connectorType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        ProductValidator.validateProductData(id, title, price, stock);
        ProductValidator.validateUniqueProduct(findById(id), id);
        if (lengthInMeters <= 0) {
            throw new InvalidDataException("longitud", "La longitud del cable debe ser mayor que cero.");
        }
        Cable cable = new Cable(lengthInMeters, connectorType, compatibleConsoleIds, id, title, price, stock);
        accessories.add(cable);
        repository.saveAll(accessories);
    }

    public void registerController(String connectionType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        ProductValidator.validateProductData(id, title, price, stock);
        ProductValidator.validateUniqueProduct(findById(id), id);
        Controller controller = new Controller(connectionType, compatibleConsoleIds, id, title, price, stock);
        accessories.add(controller);
        repository.saveAll(accessories);
    }

    public void registerMemory(int storageCapacityGb, String memoryType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        ProductValidator.validateProductData(id, title, price, stock);
        ProductValidator.validateUniqueProduct(findById(id), id);
        if (storageCapacityGb <= 0) {
            throw new InvalidDataException("capacidad", "La capacidad de almacenamiento debe ser mayor que cero.");
        }
        Memory memory = new Memory(storageCapacityGb, memoryType, compatibleConsoleIds, id, title, price, stock);
        accessories.add(memory);
        repository.saveAll(accessories);

    }
        public List<Accessory> listAllaccessories() {
        return accessories;
    }
        
    public List<Accessory> listAccesoriesByType(String type) {
        return accessories.stream().filter(accessory ->accessory.getClass().getSimpleName().equalsIgnoreCase(type)).collect(Collectors.toList());
    }
    public List<Accessory> findAcessoriesCOmpatibleWith(String consoleId) {
        return accessories.stream()
                .filter(accessory -> accessory != null && accessory.isCompatibleWith(consoleId))
                .collect(Collectors.toList());
    }
    /**
     * Updates the stock of an accessory by the given delta.
     *
     * @param accessoryId the identifier of the accessory to update
     * @param quantity the quantity to add to or subtract from the stock
     */
    public void updateStock(String accessoryId, int quantity) {
        Accessory accessory = findById(accessoryId);
        ProductValidator.validateProductExists(accessory, accessoryId);

        if (quantity < 0) {
            ProductValidator.validateStockAvailability(accessory, -quantity);
        }

        accessory.updateStock(quantity);
        repository.saveAll(accessories);
    }

    /**
     * Increases the stock of an accessory by the given quantity.
     * Used when a return is processed and the accessory goes back to inventory.
     *
     * @param accessoryId the id of the accessory to restore
     * @param quantity the quantity to add back to stock (must be positive)
     */
    public void restoreStock(String accessoryId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidDataException("cantidad", "La cantidad a restaurar debe ser positiva.");
        }
        updateStock(accessoryId, quantity);
    }
}
