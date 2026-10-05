package vn.iotstar.util;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil_24133034 {
    // Tài khoản Gmail và App Password của bạn
    private static final String FROM_EMAIL = "anhkhoi31082006@gmail.com";
    private static final String APP_PASSWORD = "bgyznyvysgjxgbdl"; // 16 ký tự mật khẩu ứng dụng Gmail

    // Sinh mã ngẫu nhiên 6 chữ số
    public static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    // Gửi email OTP thực tế
    public static boolean sendOtpEmail(String toEmail, String otpCode) {
        System.out.println("==================================================");
        System.out.println("[OTP NOTIFICATION] Đang gửi OTP tới: " + toEmail + " | Mã: " + otpCode);
        System.out.println("==================================================");

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "BookStore 24133034 - Lê Huỳnh Anh Khôi"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã OTP kích hoạt tài khoản BookStore - Lê Huỳnh Anh Khôi (24133034)");
            
            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; max-width: 600px; margin: 0 auto;'>"
                    + "<h2 style='color: #0d6efd;'>HỆ THỐNG BOOKSTORE - ĐỀ SỐ 02</h2>"
                    + "<p>Xin chào quý khách,</p>"
                    + "<p>Cảm ơn bạn đã đăng ký tài khoản tại BookStore. Mã xác thực OTP kích hoạt tài khoản của bạn là:</p>"
                    + "<div style='background-color: #f8f9fa; border: 2px dashed #0d6efd; padding: 15px; text-align: center; margin: 20px 0;'>"
                    + "<span style='font-size: 32px; font-weight: bold; color: #d63384; letter-spacing: 5px;'>" + otpCode + "</span>"
                    + "</div>"
                    + "<p style='color: #6c757d;'>Mã này có hiệu lực trong vòng 5 phút. Vui lòng không chia sẻ mã OTP này cho bất kỳ ai.</p>"
                    + "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>"
                    + "<p style='font-size: 13px; color: #999;'>Sinh viên thực hiện: <strong>Lê Huỳnh Anh Khôi</strong> - MSSV: <strong>24133034</strong></p>"
                    + "</div>";

            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("[SUCCESS] Đã gửi thành công email OTP tới " + toEmail);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        // Test gửi thử
        sendOtpEmail("anhkhoi31082006@gmail.com", generateOtp());
    }
}
