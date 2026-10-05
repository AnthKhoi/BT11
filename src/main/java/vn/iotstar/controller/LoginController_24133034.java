package vn.iotstar.controller;

import vn.iotstar.model.User_24133034;
import vn.iotstar.service.IUserService_24133034;
import vn.iotstar.service.impl.UserServiceImpl_24133034;
import vn.iotstar.util.Constant_24133034;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/login"})
public class LoginController_24133034 extends HttpServlet {

    private final IUserService_24133034 userService = new UserServiceImpl_24133034();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String error = req.getParameter("error");
        if ("access_denied".equals(error)) {
            req.setAttribute("errorMessage", "Bạn cần đăng nhập với quyền Admin để truy cập trang quản trị!");
        }
        req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Vui lòng nhập đầy đủ Email và Mật khẩu!");
            req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
            return;
        }

        User_24133034 user = userService.login(email.trim(), password.trim());
        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute(Constant_24133034.SESSION_USER, user);

            if (user.isIs_admin()) {
                resp.sendRedirect(req.getContextPath() + "/admin/home");
            } else {
                resp.sendRedirect(req.getContextPath() + "/home");
            }
        } else {
            req.setAttribute("errorMessage", "Email hoặc Mật khẩu không chính xác, hoặc tài khoản chưa kích hoạt OTP!");
            req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
        }
    }
}
