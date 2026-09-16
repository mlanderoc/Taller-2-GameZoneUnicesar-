package com.gamezone.service;

import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Memory;
import com.gamezone.model.Controller;
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
        if (findById(id) != null) {
            throw new IllegalArgumentException("A cable with this ID already exists");
        }
        Cable cable = new Cable(lengthInMeters, connectorType, compatibleConsoleIds, id, title, price, stock);
        accessories.add(cable);
        repository.saveAll(accessories);
    }

    public void registerController(String connectionType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        if (findById(id) != null) {
            throw new IllegalArgumentException("A controller with this ID already exists");
        }
        Controller controller = new Controller(connectionType, compatibleConsoleIds, id, title, price, stock);
        accessories.add(controller);
        repository.saveAll(accessories);
    }

    public void registerMemory(int storageCapacityGb, String memoryType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        if (findById(id) != null) {
            throw new IllegalArgumentException("A Memory with this ID already exists");
        }
        Memory memory = new Memory(storageCapacityGb, memoryType, compatibleConsoleIds, id, title, price, stock);
        accessories.add(memory);
        repository.saveAll(accessories);

    }
        public List<Accessory> listAllaceesory() {
        return accessories;
    }
        
    public List<Accessory> listAccesoriesByType(String type) {
        return accessories.stream().filter(accessory ->accessory.getClass().getSimpleName().equalsIgnoreCase(type)).collect(Collectors.toList());
    }
    public List<Accessory> findAcessoriesCOmpatibleWith(String consoleId) {
        return accessories.stream()
                .filter(accessory ->
                        accessory.getCompatibleConsoleIds() != null &&
                        accessory.getCompatibleConsoleIds().contains(consoleId))
                .collect(Collectors.toList());
    }
    public void updateStock(String accessoryId, int quantity) {
    Accessory accessory = findById(accessoryId);

    if (accessory == null) {
        throw new IllegalArgumentException("Accessory with this ID does not exist");
    }

    accessory.updateStock(quantity);
    repository.saveAll(accessories);
}

        
}
