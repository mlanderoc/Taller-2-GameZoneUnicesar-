package com.gamezone.service;

import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.Repository;
import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Memory;
import com.gamezone.model.Controller;
import com.gamezone.validation.ProductValidator;
import java.util.stream.Collectors;
import java.util.List;

/**
 * Manages business operations for accessories, depending on the {@link Repository}
 * abstraction (DIP), implementing {@link ProductCatalogLookup} (ISP), and filtering
 * accessory subtypes polymorphically (OCP).
 */
public class AccessoryService implements ProductCatalogLookup {

    private final Repository<Accessory> repository;
    private final List<Accessory> accessories;

    /**
     * Creates the accessory service loading the initial catalog from the repository abstraction.
     *
     * @param repository the accessory repository abstraction
     */
    public AccessoryService(Repository<Accessory> repository) {
        this.repository = repository;
        this.accessories = repository.loadAll();
    }

    /**
     * Creates the accessory service loading the initial catalog from a concrete {@link AccessoryRepository}.
     *
     * @param repository the accessory repository
     */
    public AccessoryService(AccessoryRepository repository) {
        this((Repository<Accessory>) repository);
    }

    public AccessoryService(AccessoryRepository repository, List<Accessory> accessories) {
        this((Repository<Accessory>) repository);
    }

    @Override
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
        ProductValidator.validateCableData(lengthInMeters);
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
        ProductValidator.validateMemoryData(storageCapacityGb);
        Memory memory = new Memory(storageCapacityGb, memoryType, compatibleConsoleIds, id, title, price, stock);
        accessories.add(memory);
        repository.saveAll(accessories);
    }

    public List<Accessory> listAllaccessories() {
        return accessories;
    }

    /**
     * Filters registered accessories by their subtype using the polymorphic
     * {@link Accessory#matchesAccessoryType(String)} contract (OCP).
     *
     * @param type the accessory subtype name ("Controller", "Cable", or "Memory")
     * @return list of matching accessories
     */
    public List<Accessory> listAccesoriesByType(String type) {
        return accessories.stream()
                .filter(accessory -> accessory != null && accessory.matchesAccessoryType(type))
                .collect(Collectors.toList());
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
