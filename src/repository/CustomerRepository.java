package repository;

import model.Customer;
import tools.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author mymym
 */
public class CustomerRepository {

    /**
     * Function to read data from filePath
     * using FileUtils to load data then put it in Map<String, Customer>
     * 
     * @param filePath
     * @return A Map that contains customer data from file
     */
    public Map<String, Customer> loadFromFile(String filePath) {
        Map<String, Customer> customerMap = new HashMap<>();
        List<Customer> customersList = FileUtils.loadData(filePath);
        if (customersList != null) {
            for (Customer customer : customersList) {
                if (customer != null && customer.getId() != null) {
                    //Update information from file
                    customerMap.put(customer.getId().toUpperCase(), customer);
                }
            }
        }
        return customerMap;
    }

    /**
     * Function using FileUtils to save data
     * 
     * @param filePath  Path of file to be saved
     * @param customers List of data need to saved
     */
    public void saveToFile(String filePath, List<Customer> customers) {
        FileUtils.saveData(filePath, customers);
    }

    /**
     * Function checking if path file be able to read or existed in system
     * 
     * @param filePath path
     * @return true if the file is existed and can be read, false otherwise
     */
    public boolean fileExistsAndReadable(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.canRead();
    }
}