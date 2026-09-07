
package gamezoneunicesar.model;

public class Customer extends Person{
    private String email;
    private String purchasehistory;

    public Customer() {
    }

    public Customer(String email, String purchasehistory) {
        this.email = email;
        this.purchasehistory = purchasehistory;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPurchasehistory() {
        return purchasehistory;
    }

    public void setPurchasehistory(String purchasehistory) {
        this.purchasehistory = purchasehistory;
    }
    
}