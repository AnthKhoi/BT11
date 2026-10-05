package vn.iotstar.dao.impl;

import vn.iotstar.connection.DBConnection_24133034;
import vn.iotstar.dao.IOrderDao_24133034;
import vn.iotstar.model.Book_24133034;
import vn.iotstar.model.OrderDetail_24133034;
import vn.iotstar.model.Order_24133034;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl_24133034 implements IOrderDao_24133034 {

    @Override
    public int insert(Order_24133034 order) {
        String sql = "INSERT INTO dbo.orders (user_id, receiver_name, receiver_phone, receiver_address, note, payment_method, total_money, status, created_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUser_id());
            ps.setString(2, order.getReceiver_name());
            ps.setString(3, order.getReceiver_phone());
            ps.setString(4, order.getReceiver_address());
            ps.setString(5, order.getNote());
            ps.setString(6, order.getPayment_method() != null ? order.getPayment_method() : "COD");
            ps.setBigDecimal(7, order.getTotal_money());
            ps.setString(8, order.getStatus() != null ? order.getStatus() : Order_24133034.STATUS_NEW);
            ps.setTimestamp(9, order.getCreated_date() != null ? order.getCreated_date() : new Timestamp(System.currentTimeMillis()));

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean insertDetail(OrderDetail_24133034 detail) {
        String sql = "INSERT INTO dbo.order_details (order_id, bookid, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, detail.getOrder_id());
            ps.setInt(2, detail.getBookid());
            ps.setBigDecimal(3, detail.getPrice());
            ps.setInt(4, detail.getQuantity());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE dbo.orders SET status = ? WHERE order_id = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Order_24133034 findById(int orderId) {
        String sql = "SELECT o.*, u.email AS user_email, u.fullname AS user_fullname "
                   + "FROM dbo.orders o "
                   + "LEFT JOIN dbo.users u ON o.user_id = u.id "
                   + "WHERE o.order_id = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order_24133034 order = mapResultSetToOrder(rs);
                    order.setItems(findDetailsByOrderId(orderId));
                    return order;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order_24133034> findByUserId(int userId) {
        List<Order_24133034> list = new ArrayList<>();
        String sql = "SELECT o.*, u.email AS user_email, u.fullname AS user_fullname "
                   + "FROM dbo.orders o "
                   + "LEFT JOIN dbo.users u ON o.user_id = u.id "
                   + "WHERE o.user_id = ? "
                   + "ORDER BY o.order_id DESC";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order_24133034 order = mapResultSetToOrder(rs);
                    order.setItems(findDetailsByOrderId(order.getOrder_id()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Order_24133034> findByUserIdAndStatus(int userId, String status) {
        List<Order_24133034> list = new ArrayList<>();
        String sql = "SELECT o.*, u.email AS user_email, u.fullname AS user_fullname "
                   + "FROM dbo.orders o "
                   + "LEFT JOIN dbo.users u ON o.user_id = u.id "
                   + "WHERE o.user_id = ? AND LOWER(LTRIM(RTRIM(o.status))) = LOWER(LTRIM(RTRIM(?))) "
                   + "ORDER BY o.order_id DESC";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order_24133034 order = mapResultSetToOrder(rs);
                    order.setItems(findDetailsByOrderId(order.getOrder_id()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<OrderDetail_24133034> findDetailsByOrderId(int orderId) {
        List<OrderDetail_24133034> list = new ArrayList<>();
        String sql = "SELECT od.*, b.title, b.cover_image, b.isbn, b.publisher, b.quantity AS book_stock "
                   + "FROM dbo.order_details od "
                   + "LEFT JOIN dbo.books b ON od.bookid = b.bookid "
                   + "WHERE od.order_id = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail_24133034 d = new OrderDetail_24133034();
                    d.setOrder_detail_id(rs.getInt("order_detail_id"));
                    d.setOrder_id(rs.getInt("order_id"));
                    d.setBookid(rs.getInt("bookid"));
                    d.setPrice(rs.getBigDecimal("price"));
                    d.setQuantity(rs.getInt("quantity"));

                    Book_24133034 b = new Book_24133034();
                    b.setBookid(d.getBookid());
                    b.setTitle(rs.getString("title"));
                    b.setCover_image(rs.getString("cover_image"));
                    b.setIsbn(rs.getInt("isbn"));
                    b.setPublisher(rs.getString("publisher"));
                    b.setPrice(d.getPrice());
                    b.setQuantity(rs.getInt("book_stock"));
                    d.setBook(b);

                    list.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Order_24133034> findAll() {
        List<Order_24133034> list = new ArrayList<>();
        String sql = "SELECT o.*, u.email AS user_email, u.fullname AS user_fullname "
                   + "FROM dbo.orders o "
                   + "LEFT JOIN dbo.users u ON o.user_id = u.id "
                   + "ORDER BY o.order_id DESC";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order_24133034 order = mapResultSetToOrder(rs);
                order.setItems(findDetailsByOrderId(order.getOrder_id()));
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Order_24133034> findByStatus(String status) {
        List<Order_24133034> list = new ArrayList<>();
        String sql = "SELECT o.*, u.email AS user_email, u.fullname AS user_fullname "
                   + "FROM dbo.orders o "
                   + "LEFT JOIN dbo.users u ON o.user_id = u.id "
                   + "WHERE LOWER(LTRIM(RTRIM(o.status))) = LOWER(LTRIM(RTRIM(?))) "
                   + "ORDER BY o.order_id DESC";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order_24133034 order = mapResultSetToOrder(rs);
                    order.setItems(findDetailsByOrderId(order.getOrder_id()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM dbo.orders WHERE user_id = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int countByUserIdAndStatus(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM dbo.orders WHERE user_id = ? AND LOWER(LTRIM(RTRIM(status))) = LOWER(LTRIM(RTRIM(?)))";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Order_24133034 mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order_24133034 o = new Order_24133034();
        o.setOrder_id(rs.getInt("order_id"));
        o.setUser_id(rs.getInt("user_id"));
        o.setReceiver_name(rs.getString("receiver_name"));
        o.setReceiver_phone(rs.getString("receiver_phone"));
        o.setReceiver_address(rs.getString("receiver_address"));
        o.setNote(rs.getString("note"));
        o.setPayment_method(rs.getString("payment_method"));
        o.setTotal_money(rs.getBigDecimal("total_money"));
        o.setStatus(rs.getString("status"));
        o.setCreated_date(rs.getTimestamp("created_date"));

        try {
            o.setUser_email(rs.getString("user_email"));
        } catch (SQLException ignored) {}

        try {
            o.setUser_fullname(rs.getString("user_fullname"));
        } catch (SQLException ignored) {}

        return o;
    }
}
