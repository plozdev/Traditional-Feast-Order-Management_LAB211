package repository;

import model.Customer;
import tools.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRepository {

    /**
     * 
     * @param filePath
     * @return 
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

    public void saveToFile(String filePath, List<Customer> customers) {
        FileUtils.saveData(filePath, customers);
    }

    public boolean fileExistsAndReadable(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.canRead();
    }
}