package socket;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Person implements Serializable {

    private String firstName;
    private int count ;
    private String email;

    public Person() {}

    public Person(String firstName, int count , String email) {
        this.firstName = firstName;
        this.count = count;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count.get();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", count='" + count + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
