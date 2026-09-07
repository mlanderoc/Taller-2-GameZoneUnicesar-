
package gamezone.model;

/**
 * Represents a console sold by the GameZone store. A console includes its
 * brand, model, and generation.
 */
public class Console extends Product {
    
    private String brand; 
    private String model; 
    private String generation; 
    
    /**
     * Creates a console with its common product information and
     * console-specific characteristics.
     *
     * @param id the unique identifier of the console
     * @param title the title of the console
     * @param price the price of the console
     * @param stock the available quantity in inventory
     * @param brand the brand of the console
     * @param model the model of the console
     * @param generation the generation of the console
     */
    public Console( String id, String title, double price, int stock, String brand, String model, String generation) {
        super(id, title, price, stock);
        this.brand = brand;
        this.model = model;
        this.generation = generation;
    }
    
    /**
     * Returns the brand of the console.
     *
     * @return the console brand
     */
    public String getBrand() {
        return brand;
    }
    
    /**
     * Sets the brand of the console.
     *
     * @param brand the new console brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    /**
     * Returns the model of the console.
     *
     * @return the console model
     */
    public String getModel() {
        return model;
    }
    
    /**
     * Sets the model of the console.
     *
     * @param model the new console model
     */
    public void setModel(String model) {
        this.model = model;
    }
    
    /**
     * Returns the generation of the console.
     *
     * @return the console generation
     */
    public String getGeneration() {
        return generation;
    }
    
    /**
     * Sets the generation of the console.
     *
     * @param generation the new console generation
     */
    public void setGeneration(String generation) {
        this.generation = generation;
    }
    
    /**
     * Returns a complete description of the console.
     *
     * @return a string containing the console information
     */
    @Override
    public String getDescription() {
        return "Title: " + getTitle()
                + ", Brand: " + brand
                + ", Model: " + model
                + ", Generation: " + generation;
    }
}
