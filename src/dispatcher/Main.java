package dispatcher;

import business.Customers;
import business.Orders;
import business.SetMenus;
import ui.ConsoleUI;

import java.io.File;

public class Main {
    private static final String CUSTOMER_FILE_PATH = "./data/customers.dat";
    private static final String FEAST_MENU_CSV_PATH = "./data/FeastMenu.csv";
    private static final String ORDER_FILE_PATH = "./data/orders.dat";

    public static void main(String[] args) {
        File dataDir = new File("./data");
        if (!dataDir.exists()) {
            if (!dataDir.mkdirs()) {
                System.err.println("Failed to create data directory: " + dataDir.getAbsolutePath());
                return;
            }
        }

        Customers customers = new Customers(CUSTOMER_FILE_PATH);
        SetMenus setMenus = new SetMenus(FEAST_MENU_CSV_PATH);
        Orders orders = new Orders(ORDER_FILE_PATH, customers, setMenus);

        ConsoleUI consoleUI = new ConsoleUI(customers, setMenus, orders);
        consoleUI.start();
    }

}
