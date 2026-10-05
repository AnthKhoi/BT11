package vn.iotstar.controller;

import vn.iotstar.model.Book_24133034;
import vn.iotstar.model.Cart_24133034;
import vn.iotstar.service.IBookService_24133034;
import vn.iotstar.service.impl.BookServiceImpl_24133034;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/cart", "/cart/add", "/cart/update", "/cart/remove", "/cart/clear"})
public class CartController_24133034 extends HttpServlet {

    private final IBookService_24133034 bookService = new BookServiceImpl_24133034();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession();
        Cart_24133034 cart = getCart(session);

        if ("/cart/remove".equals(path)) {
            handleRemove(req, resp, cart);
            return;
        } else if ("/cart/clear".equals(path)) {
            cart.clear();
            session.setAttribute("cart_success", "Đã làm trống giỏ hàng thành công!");
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // Mặc định là /cart -> Hiển thị trang giỏ hàng
        req.setAttribute("cart", cart);
        req.getRequestDispatcher("/views/user/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession();
        Cart_24133034 cart = getCart(session);

        if ("/cart/add".equals(path)) {
            handleAdd(req, resp, cart);
        } else if ("/cart/update".equals(path)) {
            handleUpdate(req, resp, cart);
        } else {
            resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }

    private void handleAdd(HttpServletRequest req, HttpServletResponse resp, Cart_24133034 cart) throws IOException {
        HttpSession session = req.getSession();
        String bookIdStr = req.getParameter("bookId");
        String qtyStr = req.getParameter("quantity");
        String action = req.getParameter("action"); // e.g. "buy_now"
        String redirect = req.getParameter("redirect");

        int bookId = 0;
        int quantity = 1;

        try {
            if (bookIdStr != null) bookId = Integer.parseInt(bookIdStr);
            if (qtyStr != null) quantity = Integer.parseInt(qtyStr);
        } catch (NumberFormatException ignored) {}

        if (quantity <= 0) quantity = 1;

        Book_24133034 book = bookService.findById(bookId);
        if (book == null) {
            session.setAttribute("cart_error", "Không tìm thấy thông tin cuốn sách này!");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        if (book.getQuantity() <= 0) {
            session.setAttribute("cart_error", "Cuốn sách \"" + book.getTitle() + "\" hiện tại đã hết hàng!");
            resp.sendRedirect(req.getHeader("referer") != null ? req.getHeader("referer") : (req.getContextPath() + "/home"));
            return;
        }

        String warning = cart.addItem(book, quantity);
        if (warning != null) {
            session.setAttribute("cart_warning", warning);
        } else {
            session.setAttribute("cart_success", "Đã thêm \"" + book.getTitle() + "\" vào giỏ hàng thành công!");
        }

        if ("buy_now".equalsIgnoreCase(action)) {
            resp.sendRedirect(req.getContextPath() + "/checkout");
            return;
        }

        if (redirect != null && !redirect.trim().isEmpty()) {
            resp.sendRedirect(redirect);
        } else {
            String referer = req.getHeader("referer");
            if (referer != null && !referer.contains("/cart")) {
                resp.sendRedirect(referer);
            } else {
                resp.sendRedirect(req.getContextPath() + "/cart");
            }
        }
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp, Cart_24133034 cart) throws IOException {
        HttpSession session = req.getSession();
        String bookIdStr = req.getParameter("bookId");
        String qtyStr = req.getParameter("quantity");
        String operation = req.getParameter("operation"); // "inc", "dec", "set"

        int bookId = 0;
        int quantity = 1;

        try {
            if (bookIdStr != null) bookId = Integer.parseInt(bookIdStr);
            if (qtyStr != null) quantity = Integer.parseInt(qtyStr);
        } catch (NumberFormatException ignored) {}

        if ("inc".equalsIgnoreCase(operation)) {
            if (cart.getItem(bookId) != null) {
                quantity = cart.getItem(bookId).getQuantity() + 1;
            }
        } else if ("dec".equalsIgnoreCase(operation)) {
            if (cart.getItem(bookId) != null) {
                quantity = cart.getItem(bookId).getQuantity() - 1;
            }
        }

        String msg = cart.updateQuantity(bookId, quantity);
        if (msg != null) {
            session.setAttribute("cart_warning", msg);
        } else {
            session.setAttribute("cart_success", "Cập nhật số lượng giỏ hàng thành công!");
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private void handleRemove(HttpServletRequest req, HttpServletResponse resp, Cart_24133034 cart) throws IOException {
        HttpSession session = req.getSession();
        String bookIdStr = req.getParameter("bookId");
        try {
            int bookId = Integer.parseInt(bookIdStr);
            cart.removeItem(bookId);
            session.setAttribute("cart_success", "Đã xóa sản phẩm khỏi giỏ hàng!");
        } catch (NumberFormatException ignored) {}

        resp.sendRedirect(req.getContextPath() + "/cart");
    }

    private Cart_24133034 getCart(HttpSession session) {
        Cart_24133034 cart = (Cart_24133034) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart_24133034();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
