package business;

import model.Customer;
import repository.CustomerRepository;
import tools.Workable;

import java.util.*;
import java.util.stream.Collectors;

public class Customers implements Workable<Customer> {
    private final String pathFile;
    private boolean isSaved;
    private transient final CustomerRepository repo;
    private Map<String, Customer> customerMap;

    public Customers(String pathFile) {
        super();
        this.pathFile = pathFile;
        this.repo = new CustomerRepository();
        this.customerMap = new HashMap<>();
        this.isSaved = true; 
        readFromFile();
    }

    public boolean isSaved() {
        return isSaved;
    }


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
    

    @Override
    public Customer searchById(String id) {
        return this.customerMap.get(id.toUpperCase());
    }
    

    @Override
    public void update(Customer c) {
        if (!this.customerMap.containsKey(c.getId().toUpperCase())) {
            System.out.println("Error: Customer with ID " + c.getId() + " not found. Cannot update.");
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
   

 
    public List<Customer> filterByName(String name) {
        return this.customerMap.values()
                .stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase().trim()))
                .sorted(Comparator.comparing(c->c.getLastName().toLowerCase()))
                .collect(Collectors.toList());
    }


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

    @Override
    public void saveToFile() {
        repo.saveToFile(this.pathFile, new ArrayList<>(this.customerMap.values()));
        this.isSaved = true;
        System.out.println("Customer data is saved at " + this.pathFile);
    }

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
