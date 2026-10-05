package vn.iotstar.controller;

import vn.iotstar.service.IUserService_24133034;
import vn.iotstar.service.impl.UserServiceImpl_24133034;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/verify-otp"})
public class VerifyOtpController_24133034 extends HttpServlet {

    private final IUserService_24133034 userService = new UserServiceImpl_24133034();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String action = req.getParameter("action");

        if ("resend".equals(action) && email != null) {
            userService.resendOtp(email);
            req.setAttribute("successMessage", "Mã OTP mới đã được gửi vào hòm thư (hoặc hiển thị tại Console server)!");
        }

        req.setAttribute("email", email);
        req.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String otp = req.getParameter("otp");

        if (email == null || otp == null || otp.trim().isEmpty()) {
            req.setAttribute("email", email);
            req.setAttribute("errorMessage", "Vui lòng nhập mã OTP!");
            req.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(req, resp);
            return;
        }

        boolean verified = userService.verifyOtp(email.trim(), otp.trim());
        if (verified) {
            resp.sendRedirect(req.getContextPath() + "/login?message=activated");
        } else {
            req.setAttribute("email", email);
            req.setAttribute("errorMessage", "Mã OTP không chính xác hoặc đã hết hạn! Vui lòng kiểm tra lại.");
            req.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(req, resp);
        }
    }
}
