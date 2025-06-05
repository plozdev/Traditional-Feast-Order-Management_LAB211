package repository;

import model.Order;
import tools.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author mymym
 */
public class OrderRepository {
    
    /**
     * Read data through FileUtils method into a list, then convert it to a Map
     * @param filePath
     * @return 
     */
    public Map<String, Order> loadFromFile(String filePath) {
        Map<String, Order> orderMap = new HashMap<>();
        List<Order> ordersList = FileUtils.loadData(filePath); // FileUtils.loadData return List<Order>
        if (ordersList != null) {
            for (Order order : ordersList) {
                if (order != null && order.getOrderCode() != null) {
                    orderMap.put(order.getOrderCode(), order); // Order code is unique
                }
            }
        }
        return orderMap;
    }

    /**
     * Save data in file path by FileUtils methods
     * 
     * @param filePath file path
     * @param orders   list of order need to save
     */
    public void saveToFile(String filePath, List<Order> orders) {
        FileUtils.saveData(filePath, orders); // FileUtils.saveData save List<Order>
    }
}