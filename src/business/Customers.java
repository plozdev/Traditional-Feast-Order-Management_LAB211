package business;

import model.Customer;
import repository.CustomerRepository;
import tools.Workable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages a collection of Customer objects
 * Implements operations on customers data
 * @author mymym
 */
public class Customers implements Workable<Customer> {
    private final String pathFile;
    private boolean isSaved;
    private transient final CustomerRepository repo;
    private Map<String, Customer> customerMap;

    /**
     * Constructor
     * Initialize repository, set new customer list, and read data from file
     * 
     * @param pathFile customer data path file
     */
    public Customers(String pathFile) {
        super();
        this.pathFile = pathFile;
        this.repo = new CustomerRepository();
        this.customerMap = new HashMap<>();
        this.isSaved = true; 
        readFromFile();
    }

    /**
     * Checks if the data is saving
     * 
     * @return true if data is saved, false otherwise
     */
    public boolean isSaved() {
        return isSaved;
    }


    /**
     * Adds new customer in list
     * Checks if data is existed then stop function and show message to the user
     * Set save status to true
     * 
     * @param c 
     */
    @Override
    public void addNew (Customer c) {
        if (this.customerMap.containsKey(c.getId().toUpperCase())) {
            System.out.println("Error: Customer ID " + c.getId() + " already exists.");
            return;
        }
        this.customerMap.put(c.getId(),c);
        this.isSaved = false;
        System.out.println("Customer successfully added!");

    }
    

    /**
     * Search customer information by id
     * 
     * @param id  customer id
     * @return customer that exists in system, null otherwise
     */
    @Override
    public Customer searchById(String id) {
        return this.customerMap.get(id.toUpperCase());
    }
    
    /**
     * Update customer information
     * If data is null or not exists in system, show message to user
     * Show information before and after updated
     * @param c Customer need to update
     */
    @Override
    public void update(Customer c) {
        if (c==null) {
            System.err.println("Error: customer cannot be null");
            return ;
        }
        
        if (!this.customerMap.containsKey(c.getId().toUpperCase())) {
            System.err.println("Error: Customer with ID " + c.getId() + " not found. Cannot update.");
            return;
        }

        this.customerMap.put(c.getId().toUpperCase(),c);
        this.isSaved = false;

        System.out.println("Customer successfully updated!");
        System.out.println("----------------------------------------------------------------------");
        System.out.println("| Code  | Customer Name        | Phone        | Email                |");
        System.out.println("----------------------------------------------------------------------");
        System.out.println(this.searchById(c.getId()));
        System.out.println("----------------------------------------------------------------------");
        
    }
 
    /**
     * Filter all customers have name matches or contains input name
     * 
     * @param name data to filter customers name
     * @return List of customer that contains or match the name
     */
    public List<Customer> filterByName(String name) {
        return this.customerMap.values()
                .stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase().trim()))
                .sorted(Comparator.comparing(c->c.getLastName().toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Show all of customer in system
     * If system does not have any customer then show message to user
     * Show list of customer that sort in alphabetical
     */
    @Override
    public void showAll() {
        if (this.customerMap.isEmpty()){
            System.out.println("Does not have any customer information.");
            return;
        }
        System.out.println("----------------------------------------------------------------------");
        System.out.println("| Code  | Customer Name        | Phone        | Email                |");
        System.out.println("----------------------------------------------------------------------");
        this.customerMap.values().stream()
                .sorted(Comparator.comparing(c->c.getLastName().toLowerCase()))
                .forEach(System.out::println);
        System.out.println("----------------------------------------------------------------------");
    }

    /**
     * Saving customer data to file path
     */
    @Override
    public void saveToFile() {
        repo.saveToFile(this.pathFile, new ArrayList<>(this.customerMap.values()));
        this.isSaved = true;
        System.out.println("Customer data is saved at " + this.pathFile);
    }

    /**
     * Read customer data from file path
     * If file path is not correct then it will occurs error in repository
     * If repository can't load data from path file (list of customer is null) then show warning to user.
     * Set save status to true
     */
    @Override
    public void readFromFile() {
        this.customerMap = repo.loadFromFile(this.pathFile);
        if (this.customerMap == null) {
            this.customerMap = new HashMap<>();
            System.err.println("Warning: cannot get customer data from file: " + this.pathFile);
        }
        this.isSaved = true;
    }
}
