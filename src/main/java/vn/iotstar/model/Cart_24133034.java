package vn.iotstar.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart_24133034 implements Serializable {
    private static final long serialVersionUID = 1L;

    // Sử dụng LinkedHashMap để duy trì thứ tự thêm sản phẩm
    private final Map<Integer, CartItem_24133034> items = new LinkedHashMap<>();

    public Cart_24133034() {
    }

    public Map<Integer, CartItem_24133034> getItemsMap() {
        return items;
    }

    public Collection<CartItem_24133034> getItems() {
        return items.values();
    }

    public CartItem_24133034 getItem(int bookId) {
        return items.get(bookId);
    }

    /**
     * Thêm sách vào giỏ hàng có kiểm tra tồn kho tối đa
     * @return Thông báo kết quả (null nếu thành công bình thường)
     */
    public String addItem(Book_24133034 book, int quantity) {
        if (book == null) {
            return "Sách không tồn tại!";
        }
        int stock = book.getQuantity();
        if (stock <= 0) {
            return "Sách \"" + book.getTitle() + "\" hiện đã hết hàng!";
        }

        if (quantity <= 0) {
            quantity = 1;
        }

        CartItem_24133034 existing = items.get(book.getBookid());
        int currentQty = (existing != null) ? existing.getQuantity() : 0;
        int newQty = currentQty + quantity;

        String warning = null;
        if (newQty > stock) {
            newQty = stock;
            warning = "Số lượng trong giỏ đã được điều chỉnh về mức tối đa còn trong kho (" + stock + " cuốn)!";
        }

        if (existing != null) {
            existing.setQuantity(newQty);
        } else {
            items.put(book.getBookid(), new CartItem_24133034(book, newQty));
        }

        return warning;
    }

    /**
     * Cập nhật số lượng sách trong giỏ hàng trong giới hạn tồn kho
     * @return Thông báo kết quả (null nếu thành công bình thường)
     */
    public String updateQuantity(int bookId, int quantity) {
        CartItem_24133034 item = items.get(bookId);
        if (item == null) {
            return "Sản phẩm không có trong giỏ hàng!";
        }

        if (quantity <= 0) {
            items.remove(bookId);
            return "Đã xóa sản phẩm khỏi giỏ hàng!";
        }

        int stock = item.getBook().getQuantity();
        String warning = null;
        if (quantity > stock) {
            quantity = stock;
            warning = "Số lượng chỉ được tối đa bằng lượng tồn kho (" + stock + " cuốn)!";
        }

        item.setQuantity(quantity);
        return warning;
    }

    public void removeItem(int bookId) {
        items.remove(bookId);
    }

    public void clear() {
        items.clear();
    }

    public int getTotalQuantity() {
        int total = 0;
        for (CartItem_24133034 item : items.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    // Alias cho JSP expression ${cart.totalItems}
    public int getTotalItems() {
        return getTotalQuantity();
    }

    public BigDecimal getTotalMoney() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem_24133034 item : items.values()) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
