package vn.iotstar.test;

import org.junit.Assert;
import org.junit.Test;
import vn.iotstar.model.Book_24133034;
import vn.iotstar.model.Cart_24133034;
import vn.iotstar.model.Order_24133034;
import vn.iotstar.service.IOrderService_24133034;
import vn.iotstar.service.impl.OrderServiceImpl_24133034;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderAndCartTest_24133034 {

    @Test
    public void testCartOperationsAndLimits() {
        Cart_24133034 cart = new Cart_24133034();

        Book_24133034 book1 = new Book_24133034();
        book1.setBookid(1);
        book1.setTitle("Test Book 1");
        book1.setPrice(new BigDecimal("100000"));
        book1.setQuantity(5); // Tồn kho = 5

        // 1. Thêm số lượng hợp lệ
        String warn1 = cart.addItem(book1, 2);
        Assert.assertNull(warn1);
        Assert.assertEquals(2, cart.getTotalQuantity());
        Assert.assertEquals(new BigDecimal("200000"), cart.getTotalMoney());

        // 2. Thêm vượt quá tồn kho (2 + 4 = 6 > 5) -> Phải tự giới hạn về 5
        String warn2 = cart.addItem(book1, 4);
        Assert.assertNotNull(warn2);
        Assert.assertEquals(5, cart.getItem(1).getQuantity());
        Assert.assertEquals(5, cart.getTotalQuantity());

        // 3. Cập nhật số lượng trong giới hạn
        String warn3 = cart.updateQuantity(1, 3);
        Assert.assertNull(warn3);
        Assert.assertEquals(3, cart.getItem(1).getQuantity());

        // 4. Cập nhật vượt tồn kho (10 > 5) -> Phải giới hạn về 5
        String warn4 = cart.updateQuantity(1, 10);
        Assert.assertNotNull(warn4);
        Assert.assertEquals(5, cart.getItem(1).getQuantity());

        // 5. Xóa khỏi giỏ
        cart.removeItem(1);
        Assert.assertTrue(cart.isEmpty());
    }

    @Test
    public void testOrderDatabaseAndStatusFiltering() {
        IOrderService_24133034 orderService = new OrderServiceImpl_24133034();

        // Kiểm tra lấy tất cả đơn của user 2
        List<Order_24133034> allOrders = orderService.getOrdersByUserId(2, "all");
        Assert.assertNotNull(allOrders);
        Assert.assertTrue(allOrders.size() >= 8);

        // Kiểm tra đủ 8 trạng thái yêu cầu
        String[] statuses = {
            "Đơn hàng mới",
            "Đã xác nhận",
            "Chuẩn bị hàng",
            "Vận chuyển",
            "Giao hàng",
            "Đã giao",
            "Đơn hàng hủy",
            "Đơn hàng hoàn"
        };

        Map<String, Integer> counts = orderService.countOrdersByStatusForUser(2);
        for (String st : statuses) {
            List<Order_24133034> filtered = orderService.getOrdersByUserId(2, st);
            Assert.assertFalse("Trạng thái " + st + " phải có ít nhất 1 đơn mẫu", filtered.isEmpty());
            Assert.assertEquals(filtered.size(), (int) counts.get(st));
            for (Order_24133034 o : filtered) {
                Assert.assertEquals(st.trim().toLowerCase(), o.getStatus().trim().toLowerCase());
                Assert.assertNotNull(o.getItems());
                Assert.assertFalse(o.getItems().isEmpty());
            }
        }
    }

    @Test
    public void testCreateAndCancelOrder() {
        IOrderService_24133034 orderService = new OrderServiceImpl_24133034();
        vn.iotstar.service.IBookService_24133034 bookService = new vn.iotstar.service.impl.BookServiceImpl_24133034();

        Book_24133034 bookBefore = bookService.findById(1);
        int initialStock = bookBefore.getQuantity();

        // 1. Tạo giỏ hàng
        Cart_24133034 cart = new Cart_24133034();
        cart.addItem(bookBefore, 2);

        // 2. Tạo đơn hàng COD
        Order_24133034 order = new Order_24133034();
        order.setUser_id(2);
        order.setReceiver_name("Khách Hàng Test COD");
        order.setReceiver_phone("0987654321");
        order.setReceiver_address("456 Hai Bà Trưng, Q1, TP.HCM");
        order.setNote("Giao buổi tối");
        order.setPayment_method("COD");

        int orderId = orderService.createOrder(order, cart);
        Assert.assertTrue("Order creation must succeed", orderId > 0);

        // Kiểm tra tồn kho đã bị trừ 2
        Book_24133034 bookAfterOrder = bookService.findById(1);
        Assert.assertEquals(initialStock - 2, bookAfterOrder.getQuantity());

        // Kiểm tra đơn hàng tạo ra có trạng thái "Đơn hàng mới"
        Order_24133034 createdOrder = orderService.getOrderById(orderId);
        Assert.assertNotNull(createdOrder);
        Assert.assertEquals(Order_24133034.STATUS_NEW, createdOrder.getStatus());
        Assert.assertEquals("COD", createdOrder.getPayment_method());

        // 3. Hủy đơn hàng và kiểm tra hoàn trả tồn kho
        boolean cancelSuccess = orderService.cancelOrder(orderId, 2);
        Assert.assertTrue(cancelSuccess);

        Book_24133034 bookAfterCancel = bookService.findById(1);
        Assert.assertEquals(initialStock, bookAfterCancel.getQuantity());

        Order_24133034 cancelledOrder = orderService.getOrderById(orderId);
        Assert.assertEquals(Order_24133034.STATUS_CANCELLED, cancelledOrder.getStatus());
    }
}
