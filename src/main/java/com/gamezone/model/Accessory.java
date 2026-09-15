
package com.gamezone.model;

import java.util.List;

public abstract class Accessory extends Product {
    
    private List<String> compatibleConsoleIds;

    public Accessory(List<String> compatibleConsoleIds, String id, String title, double price, int stock) {
        super(id, title, price, stock);
        this.compatibleConsoleIds = compatibleConsoleIds;
    }

   

    public List<String> getCompatibleConsoleIds() {
        return compatibleConsoleIds;
    }

    public void setCompatibleConsoleIds(List<String> compatibleConsoleIds) {
        this.compatibleConsoleIds = compatibleConsoleIds;
    }
    
    @Override
    public String getDescription(){
        return "Title: " + getTitle()
        + ", Compatible console: " + compatibleConsoleIds;
    }
    
    
}
