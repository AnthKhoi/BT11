package vn.iotstar.controller;

import vn.iotstar.model.Order_24133034;
import vn.iotstar.service.IOrderService_24133034;
import vn.iotstar.service.impl.OrderServiceImpl_24133034;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/admin/orders", "/admin/orders/update-status"})
public class AdminOrderController_24133034 extends HttpServlet {

    private final IOrderService_24133034 orderService = new OrderServiceImpl_24133034();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");
        if (status == null || status.trim().isEmpty()) {
            status = "all";
        } else {
            status = status.trim();
        }

        List<Order_24133034> orders = orderService.getAllOrders(status);
        Map<String, Integer> statusCounts = orderService.countOrdersByStatusAll();

        req.setAttribute("orders", orders);
        req.setAttribute("currentStatus", status);
        req.setAttribute("statusCounts", statusCounts);
        req.setAttribute("allStatuses", OrderServiceImpl_24133034.ALL_STATUSES);

        req.getRequestDispatcher("/views/admin/order-list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession();

        if ("/admin/orders/update-status".equals(path)) {
            String orderIdStr = req.getParameter("orderId");
            String newStatus = req.getParameter("newStatus");

            try {
                int orderId = Integer.parseInt(orderIdStr);
                boolean success = orderService.updateOrderStatus(orderId, newStatus);
                if (success) {
                    session.setAttribute("admin_order_success", "Đã cập nhật trạng thái đơn hàng #OD-" + orderId + " thành: " + newStatus);
                } else {
                    session.setAttribute("admin_order_error", "Cập nhật trạng thái thất bại!");
                }
            } catch (NumberFormatException ignored) {}

            String referer = req.getHeader("referer");
            if (referer != null) {
                resp.sendRedirect(referer);
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/orders");
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/orders");
        }
    }
}
