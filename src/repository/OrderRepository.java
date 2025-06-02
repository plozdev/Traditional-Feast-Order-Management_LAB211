package repository;

import model.Order;
import tools.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {
    public Map<String, Order> loadFromFile(String filePath) {
        Map<String, Order> orderMap = new HashMap<>();
        List<Order> ordersList = FileUtils.loadData(filePath); // FileUtils.loadData trả về List<Order>
        if (ordersList != null) {
            for (Order order : ordersList) {
                if (order != null && order.getOrderCode() != null) {
                    orderMap.put(order.getOrderCode(), order); // Order code là unique
                }
            }
        }
        return orderMap;
    }

    public void saveToFile(String filePath, List<Order> orders) {
        FileUtils.saveData(filePath, orders); // FileUtils.saveData lưu List<Order>
    }
}