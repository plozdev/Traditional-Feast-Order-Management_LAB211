package model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a customer in the system.
 * This class is serializable to allow for saving and loading customer data.
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes
    private String id;      // Unique identifier for the customer (converted to uppercase)
    private String name;    // Full name of the customer
    private String phone;   // Phone number of the customer
    private String email;   // Email address of the customer

    /**
     * Dedault constructor
     */
    public Customer() {
    }


    /**
     * Constructs a new Customer with details.
     * The customer ID is automatically converted to uppercase.
     *
     * @param id    The unique ID of the customer.
     * @param name  The name of the customer.
     * @param phone The phone number of the customer.
     * @param email The email address of the customer.
     */
    public Customer(String id, String name, String phone, String email) {
        setId(id); // Use setter to ensure ID is uppercase
        this.name = name;
        this.phone = phone;
        this.email = email;
    }


    //Getters & Setters
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


    /**
     * Extracts the last name from the customer's full name.
     * Assumes the last word in the name string is the last name.
     *
     * @return The last name of the customer, or the full name if it's a single word. Returns empty string if name is null or empty.
     */
    public String getLastName() {
        if (this.name == null || this.name.trim().isEmpty()) {
            return "";
        }

        String []parts = this.name.trim().split("\\s+");
        return parts[parts.length - 1];
    }

    /**
     * Returns a string representation of the Customer object, formatted for display in a table.
     * The name is displayed as "Last Name, First Name M. Middle Name".
     *
     * @return A formatted string with customer details.
     */
    @Override
    public String toString() {
        return String.format("| %-5s | %-20s | %-12s | %-20s |",
                id != null ? id : "N/A",
                name != null ? swapFirstName(name) : "N/A",
                phone != null ? phone : "N/A",
                email != null ? email : "N/A");
    }

    /**
     * Generates a hash code for the Customer object.
     * Based solely on the customer ID.
     *
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id); // ID is the primary key for equality
    }

    /**
     * Compares this Customer to another object for equality.
     * Two customers are considered equal if their IDs are the same (case-insensitive, due to setId behavior).
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    /**
     * Swaps the first name and last name for display purposes.
     * Converts "First M. Last" to "Last, First M."
     *
     * @param name The full name string.
     * @return The name with the last name first, followed by a comma and the rest of the name.
     */
    private String swapFirstName(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length < 2) {
            return name; // Return original name if it's a single word or empty after trim
        }
        // Join all parts except the last one to form the "first name" part
        return parts[parts.length - 1] + ", " + String.join(" ", Arrays.copyOf(parts, parts.length - 1));
    }

}
