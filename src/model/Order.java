package model;

import business.Customers;
import business.SetMenus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Order implements Serializable {
    private String orderCode, customerId, menuId;
    private int numOfTables;
    private Date eventDate;

    //Used to held references to business logic classes for display/logic after load
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
     * @param menuId the id of the setmenu
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
     * @param setMenus the setmenus business
     */
    public void setDataSource(Customers customers, SetMenus setMenus) {
        this.customers = customers;
        this.setMenus = setMenus;
    }
    public long getTotalCost(SetMenu setMenu) {
        if (setMenu==null) {
            System.err.println("Error: Can't get total cost");
            return 0;
        }
        return (setMenu.getPrice() * this.numOfTables);
    }
  
    @Override
    public String toString() {
        if (customers == null || setMenus == null) return "Data source not set.";

        Customer customer = customers.searchById(customerId.toUpperCase());
        SetMenu setMenu = setMenus.getMenuById(menuId.toUpperCase());

        if (customer == null || setMenu == null) return "Invalid customer or menu reference.";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = (this.eventDate != null) ? sdf.format(eventDate) : "N/A";
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        // For system uniqueness, orderCode is the primary identifier.
        return Objects.equals(orderCode, order.orderCode);
    }
    @Override
    public int hashCode() {
        return Objects.hash(customerId,menuId,eventDate,numOfTables,orderCode);
    }
}
