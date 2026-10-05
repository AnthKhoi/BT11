package vn.iotstar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order_24133034 implements Serializable {
    private static final long serialVersionUID = 1L;

    // 8 trạng thái theo yêu cầu đề bài
    public static final String STATUS_NEW = "Đơn hàng mới";
    public static final String STATUS_CONFIRMED = "Đã xác nhận";
    public static final String STATUS_PREPARING = "Chuẩn bị hàng";
    public static final String STATUS_SHIPPING = "Vận chuyển";
    public static final String STATUS_DELIVERING = "Giao hàng";
    public static final String STATUS_DELIVERED = "Đã giao";
    public static final String STATUS_CANCELLED = "Đơn hàng hủy";
    public static final String STATUS_RETURNED = "Đơn hàng hoàn";

    private int order_id;
    private int user_id;
    private String receiver_name;
    private String receiver_phone;
    private String receiver_address;
    private String note;
    private String payment_method = "COD";
    private BigDecimal total_money;
    private String status = STATUS_NEW;
    private Timestamp created_date;

    // View model fields
    private List<OrderDetail_24133034> items = new ArrayList<>();
    private String user_email;
    private String user_fullname;

    public Order_24133034() {
    }

    public Order_24133034(int user_id, String receiver_name, String receiver_phone, String receiver_address, String note, String payment_method, BigDecimal total_money, String status) {
        this.user_id = user_id;
        this.receiver_name = receiver_name;
        this.receiver_phone = receiver_phone;
        this.receiver_address = receiver_address;
        this.note = note;
        this.payment_method = payment_method;
        this.total_money = total_money;
        this.status = status;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public BigDecimal getTotal_money() {
        return total_money;
    }

    public void setTotal_money(BigDecimal total_money) {
        this.total_money = total_money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public List<OrderDetail_24133034> getItems() {
        return items;
    }

    public void setItems(List<OrderDetail_24133034> items) {
        this.items = items;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public int getTotalQuantity() {
        int count = 0;
        if (items != null) {
            for (OrderDetail_24133034 d : items) {
                count += d.getQuantity();
            }
        }
        return count;
    }

    /**
     * Trả về CSS class tương ứng cho badge trạng thái
     */
    public String getStatusBadgeClass() {
        if (status == null) return "bg-secondary";
        String s = status.trim().toLowerCase();
        if (s.contains("mới")) return "bg-primary";
        if (s.contains("xác nhận")) return "bg-info text-dark";
        if (s.contains("chuẩn bị")) return "bg-warning text-dark";
        if (s.contains("vận chuyển")) return "bg-secondary";
        if (s.contains("giao hàng")) return "bg-info";
        if (s.contains("đã giao")) return "bg-success";
        if (s.contains("hủy")) return "bg-danger";
        if (s.contains("hoàn")) return "bg-dark";
        return "bg-secondary";
    }

    /**
     * Trả về icon bootstrap tương ứng cho trạng thái
     */
    public String getStatusIcon() {
        if (status == null) return "bi-question-circle";
        String s = status.trim().toLowerCase();
        if (s.contains("mới")) return "bi-receipt";
        if (s.contains("xác nhận")) return "bi-check2-circle";
        if (s.contains("chuẩn bị")) return "bi-box-seam";
        if (s.contains("vận chuyển")) return "bi-truck";
        if (s.contains("giao hàng")) return "bi-bicycle";
        if (s.contains("đã giao")) return "bi-patch-check-fill";
        if (s.contains("hủy")) return "bi-x-circle-fill";
        if (s.contains("hoàn")) return "bi-arrow-counterclockwise";
        return "bi-clock-history";
    }

    /**
     * Kiểm tra có thể hủy đơn hay không (chỉ hủy được khi trạng thái là Đơn hàng mới)
     */
    public boolean isCanCancel() {
        return status != null && status.trim().equalsIgnoreCase(STATUS_NEW);
    }
}
