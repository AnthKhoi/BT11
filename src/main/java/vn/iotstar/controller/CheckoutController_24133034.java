package vn.iotstar.controller;

import vn.iotstar.model.Cart_24133034;
import vn.iotstar.model.Order_24133034;
import vn.iotstar.model.User_24133034;
import vn.iotstar.service.IOrderService_24133034;
import vn.iotstar.service.impl.OrderServiceImpl_24133034;
import vn.iotstar.util.Constant_24133034;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController_24133034 extends HttpServlet {

    private final IOrderService_24133034 orderService = new OrderServiceImpl_24133034();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User_24133034 user = (User_24133034) session.getAttribute(Constant_24133034.SESSION_USER);

        if (user == null) {
            session.setAttribute("auth_message", "Vui lòng đăng nhập để tiến hành đặt hàng và thanh toán COD!");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Cart_24133034 cart = (Cart_24133034) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        req.setAttribute("user", user);
        req.setAttribute("cart", cart);
        req.getRequestDispatcher("/views/user/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User_24133034 user = (User_24133034) session.getAttribute(Constant_24133034.SESSION_USER);

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Cart_24133034 cart = (Cart_24133034) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        String receiverName = req.getParameter("receiver_name");
        String receiverPhone = req.getParameter("receiver_phone");
        String receiverAddress = req.getParameter("receiver_address");
        String note = req.getParameter("note");
        String paymentMethod = req.getParameter("payment_method");

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            paymentMethod = "COD";
        }

        if (receiverName == null || receiverName.trim().isEmpty() ||
            receiverPhone == null || receiverPhone.trim().isEmpty() ||
            receiverAddress == null || receiverAddress.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Vui lòng điền đầy đủ Họ tên, Số điện thoại và Địa chỉ nhận hàng!");
            req.setAttribute("user", user);
            req.setAttribute("cart", cart);
            req.getRequestDispatcher("/views/user/checkout.jsp").forward(req, resp);
            return;
        }

        Order_24133034 order = new Order_24133034();
        order.setUser_id(user.getId());
        order.setReceiver_name(receiverName.trim());
        order.setReceiver_phone(receiverPhone.trim());
        order.setReceiver_address(receiverAddress.trim());
        order.setNote(note != null ? note.trim() : "");
        order.setPayment_method(paymentMethod.trim());
        order.setStatus(Order_24133034.STATUS_NEW);

        int orderId = orderService.createOrder(order, cart);
        if (orderId > 0) {
            // Xóa sạch giỏ hàng sau khi đặt thành công
            cart.clear();
            session.setAttribute("order_success", "Đặt hàng thành công với hình thức COD (Thanh toán khi nhận hàng)! Mã đơn: #OD-" + orderId);
            resp.sendRedirect(req.getContextPath() + "/order-history");
        } else {
            req.setAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình tạo đơn hàng! Vui lòng thử lại.");
            req.setAttribute("user", user);
            req.setAttribute("cart", cart);
            req.getRequestDispatcher("/views/user/checkout.jsp").forward(req, resp);
        }
    }
}
