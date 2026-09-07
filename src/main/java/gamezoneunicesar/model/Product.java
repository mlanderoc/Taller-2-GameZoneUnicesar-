
package gamezoneunicesar.model;


public abstract class Product {
    private int id;
    private String title; 
    private double price; 
    private int stock;

    public Product() {
    }

    public Product(int id, String title, double price, int stock) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public abstract String getDescription(); 
    
    public void updateStock(int quantity){
    
    this.stock += quantity; 
    
    }
    
}
