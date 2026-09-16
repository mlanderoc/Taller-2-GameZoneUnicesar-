
package com.gamezone.model;

import java.util.List;

/**
 * Represents a general accessory sold by the store.
 * An accessory extends Product and stores the identifiers
 * of the consoles it is compatible with.
 */
public abstract class Accessory extends Product {
    
    private List<String> compatibleConsoleIds;
    
    /**
     * Creates a new accessory with common product information and compatible
     * console identifiers.
     *
     * @param id unique identifier of the accessory
     * @param title title of the accessory
     * @param price price of the accessory
     * @param stock available quantity in inventory
     * @param compatibleConsoleIds identifiers of compatible consoles
     */
    public Accessory(List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        super(id, title, price, stock);
        this.compatibleConsoleIds = compatibleConsoleIds;
    }

    
    /**
     * Returns the identifiers of the consoles compatible with this accessory.
     *
     * @return a list of compatible console identifiers
     */
    public List<String> getCompatibleConsoleIds() {
        return compatibleConsoleIds;
    }
    
    /**
     * Updates the identifiers of the consoles compatible with this accessory.
     *
     * @param compatibleConsoleIds new list of compatible console identifiers
     */
    public void setCompatibleConsoleIds(List<String> compatibleConsoleIds) {
        this.compatibleConsoleIds = compatibleConsoleIds;
    }
    
    /**
     * Checks whether this accessory is compatible with a specific console.
     *
     * @param consoleId identifier of the console to check
     * @return true if the accessory is compatible with the console; otherwise
     * false
     */
    public boolean isCompatibleWith(String ConsoleId){
        for(String compatibleId : compatibleConsoleIds){
            if(compatibleId.equals(ConsoleId))
                return true; 
        }
        
        return false; 
    }
    
    /**
     * Returns a description of the accessory including its compatible consoles.
     *
     * @return formatted accessory description
     */
    @Override
    public String getDescription(){
        return "Title: " + getTitle()
        + ", Compatible console: " + compatibleConsoleIds;
    }
    
    
}
