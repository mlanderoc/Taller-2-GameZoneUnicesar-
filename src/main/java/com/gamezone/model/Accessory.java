
package com.gamezone.model;

import java.util.List;

public abstract class Accessory extends Product {
    
    private List<String> compatibleConsoleIds;

    public Accessory(String id, String title, double price, int stock) {
        super(id, title, price, stock);
    }
    
}
