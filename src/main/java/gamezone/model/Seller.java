
package gamezone.model;


public class Seller extends Person{
    private long employeecode;
    private String shift;

    public Seller() {
    }

    public Seller(long employeecode, String shift) {
        this.employeecode = employeecode;
        this.shift = shift;
    }

    public long getEmployeecode() {
        return employeecode;
    }

    public void setEmployeecode(long employeecode) {
        this.employeecode = employeecode;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
    
}