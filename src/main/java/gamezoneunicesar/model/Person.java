
package gamezoneunicesar.model;
import java.io.Serializable;

public abstract class  Person implements Serializable{
    protected long id;
    protected String firstName;
    protected String lastName;
    protected long phone;

    public Person() {
    }

    public Person(long id, String firstName, String lastName, long phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
    public String getFullName(){
        return this.firstName +" "+ this.lastName;

    }
   
}
