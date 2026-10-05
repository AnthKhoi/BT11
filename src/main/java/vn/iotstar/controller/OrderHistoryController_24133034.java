package vn.iotstar.controller;

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
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/order-history", "/order-detail", "/order-cancel"})
public class OrderHistoryController_24133034 extends HttpServlet {

    private final IOrderService_24133034 orderService = new OrderServiceImpl_24133034();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession();
        User_24133034 user = (User_24133034) session.getAttribute(Constant_24133034.SESSION_USER);

        if (user == null) {
            session.setAttribute("auth_message", "Vui lòng đăng nhập để xem lịch sử đơn hàng!");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("/order-detail".equals(path)) {
            handleOrderDetail(req, resp, user);
            return;
        }

        // Mặc định là /order-history: lọc theo trạng thái
        String status = req.getParameter("status");
        if (status == null || status.trim().isEmpty()) {
            status = "all";
        } else {
            status = status.trim();
        }

        List<Order_24133034> orders = orderService.getOrdersByUserId(user.getId(), status);
        Map<String, Integer> statusCounts = orderService.countOrdersByStatusForUser(user.getId());

        req.setAttribute("orders", orders);
        req.setAttribute("currentStatus", status);
        req.setAttribute("statusCounts", statusCounts);
        req.setAttribute("allStatuses", OrderServiceImpl_24133034.ALL_STATUSES);

        req.getRequestDispatcher("/views/user/order-history.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession();
        User_24133034 user = (User_24133034) session.getAttribute(Constant_24133034.SESSION_USER);

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("/order-cancel".equals(path)) {
            String orderIdStr = req.getParameter("orderId");
            try {
                int orderId = Integer.parseInt(orderIdStr);
                boolean success = orderService.cancelOrder(orderId, user.getId());
                if (success) {
                    session.setAttribute("order_success", "Đã hủy đơn hàng #OD-" + orderId + " thành công và hoàn trả số lượng vào kho!");
                } else {
                    session.setAttribute("order_error", "Không thể hủy đơn hàng này (chỉ đơn hàng ở trạng thái \"Đơn hàng mới\" mới có thể hủy)!");
                }
            } catch (NumberFormatException ignored) {}

            resp.sendRedirect(req.getContextPath() + "/order-history");
        } else {
            resp.sendRedirect(req.getContextPath() + "/order-history");
        }
    }

    private void handleOrderDetail(HttpServletRequest req, HttpServletResponse resp, User_24133034 user) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        try {
            int orderId = Integer.parseInt(idStr);
            Order_24133034 order = orderService.getOrderById(orderId);
            if (order != null && (order.getUser_id() == user.getId() || user.isIs_admin())) {
                req.setAttribute("order", order);
                req.getRequestDispatcher("/views/user/order-detail.jsp").forward(req, resp);
                return;
            }
        } catch (NumberFormatException ignored) {}

        resp.sendRedirect(req.getContextPath() + "/order-history");
    }
}
