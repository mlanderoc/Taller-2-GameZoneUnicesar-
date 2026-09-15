
package com.gamezone.model;

import java.util.List;

public class Controller extends Accessory {
    
    private String connectionType; 

    public Controller(String connectionType, List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        super(compatibleConsoleIds, id, title, price, stock);
        this.connectionType = connectionType;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public String getDescription() {
        return super.getDescription()
                + ", Conn  ection type: " + connectionType;
    }
    
}
