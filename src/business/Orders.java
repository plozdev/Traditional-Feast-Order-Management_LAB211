package business;

import model.Order;
import model.SetMenu;
import repository.OrderRepository;
import tools.DateUtils;
import tools.Workable;
import java.util.*;

/**
 * Manages a collection of Order objects
 * Implements operations on orders data
 * @author mymym
 */
public class Orders implements Workable<Order> {
    private final String pathFile;
    private boolean isSaved;
    private final OrderRepository repo;
    private Map<String, Order> orderMap;

    //Reference to other business classes
    private final Customers customers;
    private final SetMenus setMenus;

    /**
     * Constructor initialize repository
     * Set customers and set of menu in order
     * @param pathFile  path file
     * @param customers customer business
     * @param setMenus  set of menu business
     */
    public Orders(String pathFile, Customers customers, SetMenus setMenus) {
        this.pathFile = pathFile;
        this.repo = new OrderRepository();
        this.orderMap = new HashMap<>();
        this.customers = customers;
        this.setMenus = setMenus;
        this.isSaved = false; //No unsaved changes after loading
        readFromFile();
    }

    public boolean isSaved() { return isSaved; }


    /**
     * Checks if an order with the same customer, menu and event date already exists
     * @param x order
     * @return true if a duplicate is found, false otherwise
     */
    public boolean isDuplicate(Order x) {
        for (Order o : orderMap.values()) {
            if (o.getCustomerId().equalsIgnoreCase(x.getCustomerId()) &&
                o.getMenuId().equalsIgnoreCase(x.getMenuId()) &&
                o.getEventDate() .equals(x.getEventDate())) {
                    return true;
            }
        }
        return false;
    }

    /**
     * Adds a new order to the collection.
     * Validates customer and menu existence, and checks for duplicates before adding.
     * Sets the data source for the new order to enable detailed display.
     *
     * @param newOrder The new order to add.
     */
    @Override
    public void addNew(Order newOrder) {
        if (newOrder==null) {
            System.out.println("Error: Order data cannot be null");
            return;
        }

        if (customers.searchById(newOrder.getCustomerId()) == null) {
            System.out.println("Error: Customer not found");
            return ;
        }
        if (setMenus.searchById(newOrder.getMenuId()) == null) {
            System.out.println("Error: Menu not found");
            return ;
        }

        if (isDuplicate(newOrder)) {
            System.out.println("Order already exists.");
            return ;
        }


        newOrder.setDataSource(customers, setMenus);
        this.orderMap.put(newOrder.getOrderCode(),newOrder);
        this.isSaved = false; // Marks as unsaved
        System.out.println("Order placed successfully!");
        System.out.println(newOrder); //Display a new order
    }
   
    /**
     * Updates an existing order in the collection.
     * Validates the existence of the order and the new menu ID.
     * Sets the data source for the updated order.
     *
     * @param updatedOrder The updated order information.
     */
    @Override
    public void update(Order updateOrder) {
        if (updateOrder==null || updateOrder.getOrderCode()==null) { //If order is null
            System.out.println("Error: Invalid order data.");
            return ;
        }

        if (!this.orderMap.containsKey(updateOrder.getOrderCode())) { //If order isn't existed in system
            System.out.println("Error: Order not found");
            return ;
        }

        if (setMenus.getMenuById(updateOrder.getMenuId()) == null) { //If menu isn't existed in system
            System.out.println("Error: Menu not found");
            return;
        }

        updateOrder.setDataSource(customers,setMenus);
        this.orderMap.put(updateOrder.getOrderCode(),updateOrder);
        this.isSaved = false;
        System.out.println("Order updated successfully!"); //Success message
        System.out.println(updateOrder); //Display updated order details
    }

    /**
     * Searches order id in system
     * If found, set the data source
     * 
     * @param id
     * @return order that existed in system, null otherwise
     */
    @Override
    public Order searchById(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        Order order = this.orderMap.get(id.toUpperCase());
        if (order != null) {
            //Set data source when an order is retrieved, so its method can access related data
            order.setDataSource(customers, setMenus);
        }
        return this.orderMap.get(id);
    }

    /**
     * Shows all of orders that system contains
     * and show detail information in each order
     * If empty, show message to user
     * Sorted in time ascending
     */
    @Override
    public void showAll () {
        if (this.orderMap.isEmpty()) {
            System.out.println("Does not have any customer information.");
            return ;
        }

        List<Order> orders = new ArrayList<>(this.orderMap.values());
        orders.sort(Comparator.comparing(Order::getEventDate));

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("| %-12s | %-10s | %-11s | %-8s | %-9s | %-5s | %15s |\n",
                "ID", "Event date", "Customer ID", "Set Menu", "Price", "Table", "Cost");
        System.out.println("--------------------------------------------------------------------------------------------");

        for (Order o : orders) {
            SetMenu menu = setMenus.getMenuById(o.getMenuId());

            System.out.printf("| %-12s | %-10s | %-11s | %-8s | %,9d | %5d | %,15d |\n",
                    o.getOrderCode(),
                    DateUtils.formatDate(o.getEventDate()),
                    o.getCustomerId(),
                    o.getMenuId(),
                    menu.getPrice(),
                    o.getNumOfTables(),
                    o.getTotalCost(menu));
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    /**
     * Using repository to save data in system, with path file and list of order
     * Set save status to true and show message
     */
    @Override
    public void saveToFile() {
        repo.saveToFile(this.pathFile, new ArrayList<>(this.orderMap.values()));
        this.isSaved = true;
        System.out.println("Order data is saved at " + this.pathFile);
    }

    /**
     * Using repository to read data from file
     * If order is not exist, initialize new order list and show message
     * Set save status to true
     */
    @Override
    public void readFromFile() {
        this.orderMap = repo.loadFromFile(this.pathFile);
        if (this.orderMap == null) {
            this.orderMap = new HashMap<>();
            System.err.println("Warning: cannot get order data from file: " + this.pathFile);
        }
        this.isSaved = true;
    }
}
