package business;

import model.Order;
import model.SetMenu;
import repository.OrderRepository;
import tools.Workable;

import java.text.SimpleDateFormat;
import java.util.*;

public class Orders implements Workable<Order> {
    private final String pathFile;
    private boolean isSaved;
    private final OrderRepository repo;
    private Map<String, Order> orderMap;

    //Reference to other business classes
    private final Customers customers;
    private final SetMenus setMenus;

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
        this.isSaved = false;
        System.out.println("Order placed successfully!");
        System.out.println(newOrder);
    }
   
    @Override
    public void update(Order updateOrder) {
        if (updateOrder==null || updateOrder.getOrderCode()==null) {
            System.out.println("Error: Invalid order data.");
            return ;
        }

        if (!this.orderMap.containsKey(updateOrder.getOrderCode())) {
            System.out.println("Error: Order not found");
            return ;
        }

        if (setMenus.getMenuById(updateOrder.getMenuId()) == null) {
            System.out.println("Error: Menu not found");
            return;
        }

        updateOrder.setDataSource(customers,setMenus);
        this.orderMap.put(updateOrder.getOrderCode(),updateOrder);
        this.isSaved = false;
        System.out.println("Order updated successfully!"); //Success message
        System.out.println(updateOrder); //Display updated order details
    }

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Order o : orders) {
            SetMenu menu = setMenus.getMenuById(o.getMenuId());

            System.out.printf("| %-12s | %-10s | %-11s | %-8s | %,9d | %5d | %,15d |\n",
                    o.getOrderCode(),
                    dateFormat.format(o.getEventDate()),
                    o.getCustomerId(),
                    o.getMenuId(),
                    menu.getPrice(),
                    o.getNumOfTables(),
                    o.getTotalCost(menu));
        }
        System.out.println("--------------------------------------------------------------------------------------------");
    }

    @Override
    public void saveToFile() {
        repo.saveToFile(this.pathFile, new ArrayList<>(this.orderMap.values()));
        this.isSaved = true;
        System.out.println("Order data is saved at " + this.pathFile);
    }

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
