
package com.gamezone.model;
import java.time.LocalDate;

public abstract class Promotion {
    
    private String id;
    private String name; 
    private String startDate; 
    private String endDate; 

    public Promotion(String id, String name, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public boolean isActive(LocalDate date){
        
        return true; 
    }
    
    public abstract double calculateDiscount(Sale sale); 
    
}
