
package com.gamezone.model;

import java.util.List;

public class Cable extends Accessory {
    
    private double lengthInMeters;
    private String connectorType;

    public Cable(double lengthInMeters, String connectorType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        super(compatibleConsoleIds, id, title, price, stock);
        this.lengthInMeters = lengthInMeters;
        this.connectorType = connectorType;
    }

    public double getLengthInMeters() {
        return lengthInMeters;
    }

    public void setLengthInMeters(double lengthInMeters) {
        this.lengthInMeters = lengthInMeters;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }
    
    @Override
    public String getDescription(){
        
        return "Title: " + getTitle()
                + ", length: " + lengthInMeters
                + ",connector type: " + connectorType;
        
    }
}
