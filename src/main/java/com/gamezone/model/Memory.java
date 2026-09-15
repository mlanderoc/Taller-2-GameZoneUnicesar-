
package com.gamezone.model;

import java.util.List; 

public class Memory extends Accessory {
    
    private int storageCapacityGb;
    private String memoryType;

    public Memory(int storageCapacityGb, String memoryType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        super(compatibleConsoleIds, id, title, price, stock);
        this.storageCapacityGb = storageCapacityGb;
        this.memoryType = memoryType;
    }

    public int getStorageCapacityGb() {
        return storageCapacityGb;
    }

    public void setStorageCapacityGb(int storageCapacityGb) {
        this.storageCapacityGb = storageCapacityGb;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }
    
    @Override
    public String getDescription(){
        
        return "Title: " + getTitle()
                + ", Storage Capacity: " + storageCapacityGb + "GB"
                + ", Type: " + memoryType; 
    }
}
