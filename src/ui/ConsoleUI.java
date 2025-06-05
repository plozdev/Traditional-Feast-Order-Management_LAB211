package ui;

import business.Customers;
import business.Orders;
import business.SetMenus;
import model.Customer;
import tools.Acceptable;
import tools.DateUtils;
import tools.Inputter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import model.Order;

public class ConsoleUI {
    private final Customers customers;
    private final Orders orders;
    private final SetMenus setMenus;
    private final Scanner scanner;
    private final Inputter inputter;
    private static final String msg = "1. Register customers." + "\n" +
            "2. Update customer information." + "\n" +
            "3. Search for customer information by name." + "\n" +
            "4. Display feast menus." + "\n" +
            "5. Place a feast order." + "\n" +
            "6. Update order information." + "\n" +
            "7. Save data to file." + "\n" +
            "8. Display Customer or Order lists." + "\n" +
            "0. Quit\n";

    /**
     * Constructor 
     * Initialize customers, set of menus, and orders data from the system or create new
     * Activate scanner to get input from user
     * 
     * @param customers Customer data
     * @param setMenus  Set of menu data
     * @param orders    Orders data
     */
    public ConsoleUI(Customers customers, SetMenus setMenus, Orders orders) {
        this.customers = customers;
        this.setMenus = setMenus;
        this.orders = orders;
        this.scanner = new Scanner(System.in);
        this.inputter = new Inputter(this.scanner); // Generating Inputter
    }

    /**
     * Entry point of program
     * Show list of functions and get input from user to run one of these functions
     */
    public void start() {
        int choice;
        boolean isRunning = true;
        while (isRunning) {
            System.out.println(msg);
            choice = inputter.getInt("Enter your choice: ",
                    "Choice must be between 0 and 8.",
                    "Invalid choice format. Please enter a number.",
                    0,8);
            switch (choice) {
                case 1:
                    registerCustomerUI();
                    break;
                case 2:
                    updateCustomerUI();
                    break;
                case 3:
                    searchCustomerByNameUI();
                    break;
                case 4:
                    displayMenuUI();
                    break;
                case 5:
                    placeOrderUI();
                    break;
                case 6:
                    updateOrderUI();
                    break;
                case 7:
                    saveDataUI();
                    break;
                case 8:
                    displayListsUI();
                    break;
                case 0:
                    System.out.println("Exiting...Goodbye!");
                    isRunning = false;
                    break;
            }
        }
        scanner.close();
    }
    
    private void registerCustomerUI() {
        System.out.println("\n--- REGISTER NEW CUSTOMER ---");
        boolean continueRegistering = true;

        while (continueRegistering) {
            String id = inputter.getString(
                    "Enter Customer ID (e.g., C1234): ",
                    Acceptable.CUS_ID_VALID,
                    "Invalid ID. Must be C/G/K followed by 4 digits."
            ).toUpperCase();
            if (customers.searchById(id)!=null) {
                System.out.println("Customer ID " + id + " already exists. Please use a different ID.");
                continue; // Ask for ID again
            }

            String name = inputter.getString(
                    "Enter Name (2-25 characters): ",
                    Acceptable.NAME_VALID,
                    "Invalid name. Must be 2-25 characters."
            );
            String phone = inputter.getString(
                    "Enter Phone Number (10 digits, Vietnamese): ",
                    Acceptable.PHONE_VALID,
                    "Invalid phone. Must be 10 digits."
            );
            String email = inputter.getString(
                    "Enter Email (e.g., example@domain.com): ",
                    Acceptable.EMAIL_VALID,
                    "Invalid email format."
            );

            Customer newCustomer = new Customer(id, name, phone, email);
            customers.addNew(newCustomer);

            continueRegistering = inputter.getYesNo("Continue entering new customers?"); //Ask for continue or stop
        }
    }

    private void updateCustomerUI() {
        System.out.println("\n--- UPDATE CUSTOMER INFORMATION ---");
        boolean continueUpdating = true;

        while(continueUpdating) {
            String id = inputter.getString(
                    "Enter Customer ID to update: ",
                    Acceptable.CUS_ID_VALID,
                    "Invalid customer ID format.", true
            ).toUpperCase();

            if (id.isEmpty()){
                System.out.println("Update operation cancelled by user.");
                break;
            }

            Customer existingCustomer = customers.searchById(id);
            if (existingCustomer == null) {
                System.out.println("This customer does not exist.");
            } else {
                //Show information before update
                System.out.println("----------------------------------------------------------------------");
                System.out.println("| Code  | Customer Name        | Phone        | Email                |");
                System.out.println("----------------------------------------------------------------------");
                System.out.println(customers.searchById(id));
                System.out.println("----------------------------------------------------------------------");

                //Input customer new information 
                System.out.println("Enter new information (leave blank to keep current):");
                String newName = inputter.getString("New Name: ", Acceptable.NAME_VALID, "Invalid name.", true);
                String newPhone = inputter.getString("New Phone: ", Acceptable.PHONE_VALID, "Invalid phone.", true);
                String newEmail = inputter.getString("New Email: ", Acceptable.EMAIL_VALID, "Invalid email.", true);


                Customer updatedCustomer = new Customer(
                        existingCustomer.getId(),
                        newName.isEmpty() ? existingCustomer.getName() : newName,
                        newPhone.isEmpty() ? existingCustomer.getPhone() : newPhone,
                        newEmail.isEmpty() ? existingCustomer.getEmail() : newEmail
                );
                customers.update(updatedCustomer);
            }

            continueUpdating = inputter.getYesNo("Continue with another update?"); 
        }
    }

    private void searchCustomerByNameUI() {
        System.out.println("\n--- SEARCH CUSTOMERS BY NAME ---");
        String nameQuery = inputter.getString(
                "Enter full or partial name to search: ",
                Acceptable.STRING_NOT_EMPTY_VALID,
                "Search query cannot be empty."
        );
        List<Customer> results = customers.filterByName(nameQuery);

        if (results.isEmpty()) {
            System.out.println("No one matches the search criteria!");
        } else {
            System.out.println("----------------------------------------------------------------------");
            System.out.println("| Code  | Customer Name        | Phone        | Email                |");
            System.out.println("----------------------------------------------------------------------");
            for (Customer c : results) System.out.println(c);
            System.out.println("----------------------------------------------------------------------");
        }
        System.out.println("Press Enter to return to the main menu...");
        scanner.nextLine();
    }

    private void displayMenuUI() {
        setMenus.showMenuList();
        System.out.println("Press Enter to return to the main menu...");
        scanner.nextLine();
    }

    private void placeOrderUI() {
        System.out.println("\n--- PLACE A FEAST ORDER ---");
        boolean continuePlacing = true;

        while(continuePlacing) {
            String customerId = inputter.getString(
                    "Enter Customer ID: ", Acceptable.CUS_ID_VALID, "Invalid customer ID."
            ).toUpperCase();
            String menuId = inputter.getString(
                    "Enter Set Menu ID (e.g., PW003): ", Acceptable.MENU_ID_VALID, "Invalid menu ID."
            ).toUpperCase();

            int numOfTables = inputter.getInt(
                    "Enter Number of Tables: ", "Tables must be > 0.", "Invalid number.", 0,Integer.MAX_VALUE
            );

            Date eventDate;
            while(true) {
                eventDate = inputter.getDate(
                        "Enter Preferred Event Date (dd/MM/yyyy): ",
                        "Invalid date format.", "Date parsing failed."
                );
                if (eventDate.after(new Date())) { break; }
                System.out.println("Event date must be in the future.");
            }
            Order newOrder = new Order(customerId, menuId, numOfTables, eventDate); 
            orders.addNew(newOrder);

            continuePlacing = inputter.getYesNo("Place another order?");
        }
    }

    private void updateOrderUI() {
        System.out.println("\n--- UPDATE ORDER INFORMATION ---");
        boolean continueUpdating = true;

        while(continueUpdating) {
            String orderCodeToUpdate = inputter.getString(
                    "Enter order code to update (e.g., ORD-12345678): ", Acceptable.ORDER_CODE_VALID, "Order code cannot be empty."
            ).toUpperCase();

            Order existingOrder = orders.searchById(orderCodeToUpdate);
            if (existingOrder == null) {
                System.out.println("This order does not exist.");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                System.out.println("Updating order: " + existingOrder.getOrderCode());
                System.out.println("Current details: \nMenuID = " + existingOrder.getMenuId() +
                        ", \nTables = " + existingOrder.getNumOfTables() +
                        ", \nDate = " + sdf.format(existingOrder.getEventDate()));

                if (existingOrder.getEventDate().before(new Date())) {
                    System.out.println("This order's event date has passed. It cannot be updated.");
                } else {
                    System.out.println("Enter new information (leave blank to keep current):");

                    String newMenuId = inputter.getString("New Set Menu ID: ", Acceptable.MENU_ID_VALID, "Invalid menu ID.", true).toUpperCase();
                    String newNumOfTablesStr = inputter.getString("New Number of Tables (must be > 0): ", Acceptable.INTEGER_VALID, "Invalid number.", true);
                    String newEventDateStr = inputter.getString("New Event Date (dd/MM/yyyy): ", Acceptable.DATE_VALID, "Invalid date format.", true);

                    Order tempOrder = new Order(); // Create a temporary holder for new values
                    tempOrder.setOrderCode(existingOrder.getOrderCode());
                    tempOrder.setCustomerId(existingOrder.getCustomerId()); // Customer ID usually not changed for an order

                    tempOrder.setMenuId(newMenuId.isEmpty() ? existingOrder.getMenuId() : newMenuId);

                    int newNumOfTables = existingOrder.getNumOfTables();
                    if (!newNumOfTablesStr.isEmpty()) newNumOfTables = Integer.parseInt(newNumOfTablesStr);
                    tempOrder.setNumOfTables(newNumOfTables);

                    Date newEventDate = existingOrder.getEventDate();
                    if (!newEventDateStr.isEmpty()) newEventDate = DateUtils.parseDate(newEventDateStr);
                    tempOrder.setEventDate(newEventDate);

                    orders.update(tempOrder);
                }
            }
            continueUpdating = inputter.getYesNo("Update another order?"); 
        }
    }

    private void saveDataUI() {
        System.out.println("\n--- SAVE DATA TO FILE ---");
        System.out.println("1. Save Customer Data");
        System.out.println("2. Save Order Data");
        System.out.println("3. Save Both");
        System.out.println("0. Cancel");
        int choice = inputter.getInt("Choice: ", "Must be 0-3", "Invalid", 0, 3);

        if (choice == 1 || choice == 3) customers.saveToFile();
        if (choice == 2 || choice == 3) orders.saveToFile();
        if (choice == 0) System.out.println("Save cancelled.");

        System.out.println("Press Enter to return to main menu...");
        scanner.nextLine();
    }

    private void displayListsUI() {
        System.out.println("\n--- DISPLAY LISTS ---");
        System.out.println("1. Display Customer List");
        System.out.println("2. Display Order List");
        System.out.println("0. Return to Main Menu");
        int choice = inputter.getInt("Choice: ", "Must be 0-2", "Invalid", 0, 2);

        if (choice == 1) { // Hiển thị danh sách khách hàng
            customers.showAll();
        } else if (choice == 2) { // Hiển thị danh sách đặt hàng
            orders.showAll();
        }
        System.out.println("Press Enter to return to main menu...");
        scanner.nextLine();
    }
}
