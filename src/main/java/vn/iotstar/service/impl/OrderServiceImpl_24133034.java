package vn.iotstar.service.impl;

import vn.iotstar.connection.DBConnection_24133034;
import vn.iotstar.dao.IOrderDao_24133034;
import vn.iotstar.dao.impl.OrderDaoImpl_24133034;
import vn.iotstar.model.CartItem_24133034;
import vn.iotstar.model.Cart_24133034;
import vn.iotstar.model.OrderDetail_24133034;
import vn.iotstar.model.Order_24133034;
import vn.iotstar.service.IOrderService_24133034;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderServiceImpl_24133034 implements IOrderService_24133034 {

    private final IOrderDao_24133034 orderDao = new OrderDaoImpl_24133034();

    // 8 trạng thái theo đúng yêu cầu đề bài
    public static final String[] ALL_STATUSES = {
        Order_24133034.STATUS_NEW,          // "Đơn hàng mới"
        Order_24133034.STATUS_CONFIRMED,    // "Đã xác nhận"
        Order_24133034.STATUS_PREPARING,    // "Chuẩn bị hàng"
        Order_24133034.STATUS_SHIPPING,     // "Vận chuyển"
        Order_24133034.STATUS_DELIVERING,   // "Giao hàng"
        Order_24133034.STATUS_DELIVERED,    // "Đã giao"
        Order_24133034.STATUS_CANCELLED,    // "Đơn hàng hủy"
        Order_24133034.STATUS_RETURNED      // "Đơn hàng hoàn"
    };

    @Override
    public int createOrder(Order_24133034 order, Cart_24133034 cart) {
        if (order == null || cart == null || cart.isEmpty()) {
            return -1;
        }

        order.setTotal_money(cart.getTotalMoney());
        if (order.getStatus() == null || order.getStatus().trim().isEmpty()) {
            order.setStatus(Order_24133034.STATUS_NEW);
        }
        if (order.getCreated_date() == null) {
            order.setCreated_date(new Timestamp(System.currentTimeMillis()));
        }

        int orderId = orderDao.insert(order);
        if (orderId <= 0) {
            return -1;
        }

        order.setOrder_id(orderId);

        // Lưu chi tiết đơn hàng và trừ số lượng tồn kho của sách
        for (CartItem_24133034 item : cart.getItems()) {
            OrderDetail_24133034 detail = new OrderDetail_24133034();
            detail.setOrder_id(orderId);
            detail.setBookid(item.getBook().getBookid());
            detail.setPrice(item.getBook().getPrice());
            detail.setQuantity(item.getQuantity());

            orderDao.insertDetail(detail);

            // Trừ tồn kho sách trong database
            decreaseBookStock(item.getBook().getBookid(), item.getQuantity());
        }

        return orderId;
    }

    @Override
    public boolean cancelOrder(int orderId, int userId) {
        Order_24133034 order = orderDao.findById(orderId);
        if (order == null || order.getUser_id() != userId) {
            return false;
        }

        // Chỉ cho phép hủy khi đang ở trạng thái "Đơn hàng mới"
        if (!order.isCanCancel()) {
            return false;
        }

        boolean updated = orderDao.updateStatus(orderId, Order_24133034.STATUS_CANCELLED);
        if (updated) {
            // Hoàn lại số lượng tồn kho sách
            if (order.getItems() != null) {
                for (OrderDetail_24133034 item : order.getItems()) {
                    increaseBookStock(item.getBookid(), item.getQuantity());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String newStatus) {
        if (newStatus == null || newStatus.trim().isEmpty()) {
            return false;
        }
        return orderDao.updateStatus(orderId, newStatus.trim());
    }

    @Override
    public Order_24133034 getOrderById(int orderId) {
        return orderDao.findById(orderId);
    }

    @Override
    public List<Order_24133034> getOrdersByUserId(int userId, String status) {
        if (status == null || status.trim().isEmpty() || "all".equalsIgnoreCase(status.trim())) {
            return orderDao.findByUserId(userId);
        }
        return orderDao.findByUserIdAndStatus(userId, status.trim());
    }

    @Override
    public List<Order_24133034> getAllOrders(String status) {
        if (status == null || status.trim().isEmpty() || "all".equalsIgnoreCase(status.trim())) {
            return orderDao.findAll();
        }
        return orderDao.findByStatus(status.trim());
    }

    @Override
    public Map<String, Integer> countOrdersByStatusForUser(int userId) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        int total = orderDao.countByUserId(userId);
        counts.put("all", total);

        for (String st : ALL_STATUSES) {
            int cnt = orderDao.countByUserIdAndStatus(userId, st);
            counts.put(st, cnt);
        }
        return counts;
    }

    @Override
    public Map<String, Integer> countOrdersByStatusAll() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        List<Order_24133034> all = orderDao.findAll();
        counts.put("all", all.size());

        for (String st : ALL_STATUSES) {
            int c = 0;
            for (Order_24133034 o : all) {
                if (o.getStatus() != null && o.getStatus().trim().equalsIgnoreCase(st.trim())) {
                    c++;
                }
            }
            counts.put(st, c);
        }
        return counts;
    }

    private void decreaseBookStock(int bookId, int quantity) {
        String sql = "UPDATE dbo.books SET quantity = CASE WHEN quantity >= ? THEN quantity - ? ELSE 0 END WHERE bookid = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, quantity);
            ps.setInt(3, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void increaseBookStock(int bookId, int quantity) {
        String sql = "UPDATE dbo.books SET quantity = quantity + ? WHERE bookid = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
