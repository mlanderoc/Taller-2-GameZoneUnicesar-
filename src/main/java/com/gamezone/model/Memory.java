
package com.gamezone.model;

import java.util.List; 

/**
 * Represents a memory accessory sold by the store.
 * A memory accessory has a storage capacity and a memory type.
 */
public class Memory extends Accessory {
    
    private int storageCapacityGb;
    private String memoryType;

    /**
     * Creates a new memory accessory with its product information,
     * compatible consoles, storage capacity, and memory type.
     *
     * @param id unique identifier of the memory accessory
     * @param title title of the memory accessory
     * @param price price of the memory accessory
     * @param stock available quantity in inventory
     * @param compatibleConsoleIds identifiers of compatible consoles
     * @param storageCapacityGb storage capacity in gigabytes
     * @param memoryType memory type
     */
    public Memory(int storageCapacityGb, String memoryType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        super(compatibleConsoleIds, id, title, price, stock);
        this.storageCapacityGb = storageCapacityGb;
        this.memoryType = memoryType;
    }

    /**
     * Returns the storage capacity in gigabytes.
     *
     * @return storage capacity in gigabytes
     */

    public int getStorageCapacityGb() {
        return storageCapacityGb;
    }

    /**
     * Updates the storage capacity in gigabytes.
     *
     * @param storageCapacityGb new storage capacity in gigabytes
     */
    public void setStorageCapacityGb(int storageCapacityGb) {
        this.storageCapacityGb = storageCapacityGb;
    }

    /**
     * Returns the memory type.
     *
     * @return memory type
     */
    public String getMemoryType() {
        return memoryType;
    }

    /**
     * Updates the memory type.
     *
     * @param memoryType new memory type
     */
    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }
    
    /**
     * Returns a description of the memory accessory including its capacity and type.
     *
     * @return formatted memory accessory description
     */
    @Override
    public String getDescription(){
        
        return "Title: " + getTitle()
                + ", Storage Capacity: " + storageCapacityGb + "GB"
                + ", Type: " + memoryType; 
    }
}
