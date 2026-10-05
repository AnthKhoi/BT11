package vn.iotstar.dao;

import vn.iotstar.model.OrderDetail_24133034;
import vn.iotstar.model.Order_24133034;

import java.util.List;

public interface IOrderDao_24133034 {
    int insert(Order_24133034 order);
    boolean insertDetail(OrderDetail_24133034 detail);
    boolean updateStatus(int orderId, String status);
    Order_24133034 findById(int orderId);
    List<Order_24133034> findByUserId(int userId);
    List<Order_24133034> findByUserIdAndStatus(int userId, String status);
    List<OrderDetail_24133034> findDetailsByOrderId(int orderId);
    List<Order_24133034> findAll();
    List<Order_24133034> findByStatus(String status);
    int countByUserId(int userId);
    int countByUserIdAndStatus(int userId, String status);
}
