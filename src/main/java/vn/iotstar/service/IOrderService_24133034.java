package vn.iotstar.service;

import vn.iotstar.model.Cart_24133034;
import vn.iotstar.model.Order_24133034;

import java.util.List;
import java.util.Map;

public interface IOrderService_24133034 {
    int createOrder(Order_24133034 order, Cart_24133034 cart);
    boolean cancelOrder(int orderId, int userId);
    boolean updateOrderStatus(int orderId, String newStatus);
    Order_24133034 getOrderById(int orderId);
    List<Order_24133034> getOrdersByUserId(int userId, String status);
    List<Order_24133034> getAllOrders(String status);
    Map<String, Integer> countOrdersByStatusForUser(int userId);
    Map<String, Integer> countOrdersByStatusAll();
}
