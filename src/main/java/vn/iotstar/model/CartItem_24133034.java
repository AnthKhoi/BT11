package vn.iotstar.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem_24133034 implements Serializable {
    private static final long serialVersionUID = 1L;

    private Book_24133034 book;
    private int quantity;

    public CartItem_24133034() {
    }

    public CartItem_24133034(Book_24133034 book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    public Book_24133034 getBook() {
        return book;
    }

    public void setBook(Book_24133034 book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        if (book != null && book.getPrice() != null) {
            return book.getPrice().multiply(new BigDecimal(quantity));
        }
        return BigDecimal.ZERO;
    }
}
