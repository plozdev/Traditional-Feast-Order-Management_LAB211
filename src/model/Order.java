package model;

import business.Customers;
import business.SetMenus;
import tools.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a customer order in the system.
 * Each order has a unique code, customer ID, menu ID, number of tables, and event date.
 * This class is serializable for data persistence.
 */
public class Order implements Serializable {
//    private static final long serialVersionUID = 2L; // Version UID for serialization
    private String orderCode;     // Unique code for the order
    private String customerId;    // ID of the customer who placed the order
    private String menuId;        // ID of the set menu ordered
    private int numOfTables;      // Number of tables for the event
    private Date eventDate;       // Date of the event

    //Used to hold references to business logic classes for display/logic after load
    private transient Customers customers;
    private transient SetMenus setMenus;

    /**
     * Generates a unique order code.
     * Ex: ORD- followed by o portion of a UUID
     * @return A unique order code string
     */
    private String generateOrderCode() {
        //using UUID for a higher chance of uniqueness
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    //Default constructor
    public Order() {
        this.orderCode = generateOrderCode();
        this.customerId = this.menuId = "";
        this.numOfTables = 0;
        this.eventDate = new Date();
    }

    /**
     * Constructor to create a new order
     * The order code is generated automatically
     * @param customerId the id of the customer
     * @param menuId the id of the set menu
     * @param numOfTables the number of table for the order
     * @param date the date of the event
     */
    public Order(String customerId, String menuId, int numOfTables, Date date) {
        this.orderCode = generateOrderCode();
        this.customerId = customerId;
        this.menuId = menuId;
        this.numOfTables = numOfTables;
        this.eventDate = date;
    }

    //getters & setter
    public String getOrderCode() {
        return orderCode;
    }
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public int getNumOfTables() {
        return numOfTables;
    }
    public void setNumOfTables(int numOfTables) {
        this.numOfTables = numOfTables;
    }
    public Date getEventDate() {
        return eventDate;
    }
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Sets the transient data sources for this Order instance
     * This allows the order retrieve related information
     * @param customers the customers business
     * @param setMenus the set menus business
     */
    public void setDataSource(Customers customers, SetMenus setMenus) {
        this.customers = customers;
        this.setMenus = setMenus;
    }

    /**
     * Calculates the total cost of the order.
     * Total cost = SetMenu price * number of tables.
     *
     * @param setMenu The SetMenu object corresponding to this order's menuId.
     * @return The total cost of the order. Returns 0 if the SetMenu is null.
     */
    public long getTotalCost(SetMenu setMenu) {
        if (setMenu==null) {
            System.err.println("Error: Can't get total cost");
            return 0;
        }
        return (setMenu.getPrice() * this.numOfTables);
    }

    /**
     * Returns a string representation of the Order object, including detailed customer and menu information.
     * Requires data sources (Customers and SetMenus) to be set via {@link #setDataSource(Customers, SetMenus)}.
     *
     * @return A formatted string with detailed order information, or an error message if data sources are not set or references are invalid.
     */
    @Override
    public String toString() {
        if (customers == null || setMenus == null) return "Data source not set.";

        Customer customer = customers.searchById(customerId.toUpperCase());
        SetMenu setMenu = setMenus.getMenuById(menuId.toUpperCase());

        if (customer == null || setMenu == null) return "Invalid customer or menu reference.";

        String dateStr = (this.eventDate != null) ? DateUtils.formatDate(eventDate) : "N/A";
        long totalCost = getTotalCost(setMenu);



        return String.format(
                "------------------------------------------------------------\n" +
                        "Customer order information [Order ID: %s]\n" +
                        "------------------------------------------------------------\n" +
                        "Customer code  : %s\n" +
                        "Customer name  : %s\n" +
                        "Phone number   : %s\n" +
                        "Email          : %s\n" +
                        "------------------------------------------------------------\n" +
                        "Code of Set Menu : %s\n" +
                        "Set menu name    : %s\n" +
                        "Event date       : %s\n" +
                        "Number of tables : %d\n" +
                        "Price            : %,d Vnd\n" +
                        "Ingredients:\n%s\n" +
                        "------------------------------------------------------------\n" +
                        "Total cost       : %,d Vnd\n" +
                        "------------------------------------------------------------",
                this.orderCode,
                customer.getId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                setMenu.getMenuId(),
                setMenu.getMenuName(),
                dateStr,
                this.numOfTables,
                setMenu.getPrice(),
                setMenu.getIngredients().trim(),
                totalCost
        );
    }

    /**
     * Compares this Order to another object for equality.
     * Two orders are considered equal if their order codes are the same.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        // For system uniqueness, orderCode is the primary identifier.
        return Objects.equals(orderCode, order.orderCode);
    }

    /**
     * Generates a hash code for the Order object.
     *
     * @return The hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(customerId,menuId,eventDate,numOfTables,orderCode);
    }
}
