package vn.iotstar.controller;

import vn.iotstar.model.User_24133034;
import vn.iotstar.service.IUserService_24133034;
import vn.iotstar.service.impl.UserServiceImpl_24133034;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/register"})
public class RegisterController_24133034 extends HttpServlet {

    private final IUserService_24133034 userService = new UserServiceImpl_24133034();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phoneStr = req.getParameter("phone");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Email và mật khẩu không được để trống!");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Mật khẩu xác nhận không khớp!");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (userService.checkExistEmail(email.trim())) {
            req.setAttribute("errorMessage", "Email này đã được sử dụng! Vui lòng chọn email khác.");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }

        int phone = 0;
        try {
            if (phoneStr != null && !phoneStr.trim().isEmpty()) {
                phone = Integer.parseInt(phoneStr.trim());
            }
        } catch (NumberFormatException ignored) {}

        User_24133034 user = new User_24133034();
        user.setEmail(email.trim());
        user.setFullname(fullname != null ? fullname.trim() : "");
        user.setPhone(phone);
        user.setPasswd(password.trim());
        user.setIs_admin(false);

        boolean success = userService.register(user);
        if (success) {
            resp.sendRedirect(req.getContextPath() + "/verify-otp?email=" + email.trim() + "&message=otp_sent");
        } else {
            req.setAttribute("errorMessage", "Đăng ký không thành công. Vui lòng thử lại!");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
        }
    }
}
