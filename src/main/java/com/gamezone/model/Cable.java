
package com.gamezone.model;

import java.util.List;

/**
 * Represents a cable accessory sold by the store.
 * A cable has a length in meters and a connector type.
 */
public class Cable extends Accessory {
    
    private double lengthInMeters;
    private String connectorType;
    
    /**
     * Creates a new cable with its product information,
     * compatible consoles, length, and connector type.
     *
     * @param id unique identifier of the cable
     * @param title title of the cable
     * @param price price of the cable
     * @param stock available quantity in inventory
     * @param compatibleConsoleIds identifiers of compatible consoles
     * @param lengthInMeters cable length in meters
     * @param connectorType cable connector type
     */
    public Cable(double lengthInMeters, String connectorType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        super(compatibleConsoleIds, id, title, price, stock);
        this.lengthInMeters = lengthInMeters;
        this.connectorType = connectorType;
    }

     /**
     * Returns the cable length in meters.
     *
     * @return cable length in meters
     */
    public double getLengthInMeters() {
        return lengthInMeters;
    }
    
    /**
     * Updates the cable length in meters.
     *
     * @param lengthInMeters new cable length in meters
     */
    public void setLengthInMeters(double lengthInMeters) {
        this.lengthInMeters = lengthInMeters;
    }   
    /**
     * Returns the connector type of the cable.
     *
     * @return cable connector type
     */

    public String getConnectorType() {
        return connectorType;
    }
    
    /**
     * Updates the connector type of the cable.
     *
     * @param connectorType new cable connector type
     */

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }
    
    /**
     * Returns a description of the cable including its length and connector type.
     *
     * @return formatted cable description
     */
    @Override
    public String getDescription(){
        
        return "Title: " + getTitle()
                + ", length: " + lengthInMeters
                + ",connector type: " + connectorType;
        
    }
}
