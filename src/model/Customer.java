package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, name, phone, email;

    public Customer() {
    }

    public Customer(String id, String name, String phone, String email) {
        setId(id);
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        String []parts = this.name.trim().split("\\s+");
        return parts[parts.length - 1];
    }

    @Override
    public String toString() {
        return String.format("| %-5s | %-20s | %-12s | %-20s |", id, swapFirstName(name), phone, email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    private String swapFirstName(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length < 2) {
            return name;
        }
        return parts[parts.length - 1] + ", " + String.join(" ", Arrays.copyOf(parts, parts.length - 1));
    }

}
