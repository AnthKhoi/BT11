package vn.iotstar.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDetail_24133034 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int order_detail_id;
    private int order_id;
    private int bookid;
    private BigDecimal price;
    private int quantity;

    // View model field
    private Book_24133034 book;

    public OrderDetail_24133034() {
    }

    public OrderDetail_24133034(int order_id, int bookid, BigDecimal price, int quantity) {
        this.order_id = order_id;
        this.bookid = bookid;
        this.price = price;
        this.quantity = quantity;
    }

    public int getOrder_detail_id() {
        return order_detail_id;
    }

    public void setOrder_detail_id(int order_detail_id) {
        this.order_detail_id = order_detail_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getBookid() {
        return bookid;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Book_24133034 getBook() {
        return book;
    }

    public void setBook(Book_24133034 book) {
        this.book = book;
    }

    public BigDecimal getSubtotal() {
        if (price != null) {
            return price.multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }
}
