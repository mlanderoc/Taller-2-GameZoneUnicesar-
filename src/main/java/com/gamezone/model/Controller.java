
package com.gamezone.model;

import java.util.List;

/**
 * Represents a game controller accessory.
 * A controller can be wired or wireless and can be compatible
 * with one or more consoles.
 */
public class Controller extends Accessory {
    
    private String connectionType; 

    /**
     * Creates a new controller with its product information,
     * compatible consoles, and connection type.
     *
     * @param id unique identifier of the controller
     * @param title title of the controller
     * @param price price of the controller
     * @param stock available quantity in inventory
     * @param compatibleConsoleIds identifiers of compatible consoles
     * @param connectionType controller connection type
     */
    public Controller(String connectionType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        super(compatibleConsoleIds, id, title, price, stock);
        this.connectionType = connectionType;
    }
    
    /**
     * Returns the connection type of the controller.
     *
     * @return the controller connection type
     */
    public String getConnectionType() {
        return connectionType;
    }
    
    /**
     * Updates the connection type of the controller.
     *
     * @param connectionType new controller connection type
     */
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
    
     /**
     * Returns a description of the controller including its connection type.
     *
     * @return formatted controller description
     */
    @Override
    public String getDescription() {
        return super.getDescription()
                + ", Conection type: " + connectionType;
    }
    
}
