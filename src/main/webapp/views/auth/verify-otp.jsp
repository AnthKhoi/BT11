<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Xác thực OTP - BookStore</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow-sm border-0 mt-4">
            <div class="card-header bg-warning text-dark text-center py-3">
                <h4 class="mb-0"><i class="bi bi-shield-check"></i> XÁC THỰC MÃ OTP</h4>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        <i class="bi bi-exclamation-triangle-fill"></i> ${errorMessage}
                    </div>
                </c:if>
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success" role="alert">
                        <i class="bi bi-check-circle-fill"></i> ${successMessage}
                    </div>
                </c:if>
                <c:if test="${param.message == 'otp_sent'}">
                    <div class="alert alert-info" role="alert">
                        Mã OTP đã được gửi đến email <strong>${email}</strong>. Vui lòng kiểm tra hộp thư hoặc bảng Console của ứng dụng để lấy mã.
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/verify-otp" method="post">
                    <input type="hidden" name="email" value="${email}">

                    <div class="mb-3 text-center">
                        <label class="form-label fw-bold">Nhập mã OTP (6 chữ số):</label>
                        <input type="text" name="otp" class="form-control text-center fs-4 letter-spacing-2" 
                               maxlength="6" placeholder="______" required autofocus>
                    </div>

                    <div class="d-grid mt-4">
                        <button type="submit" class="btn btn-warning btn-block fw-bold">Xác Nhận Kích Hoạt</button>
                    </div>
                </form>

                <div class="text-center mt-4">
                    <span>Chưa nhận được mã? </span>
                    <a href="${pageContext.request.contextPath}/verify-otp?email=${email}&action=resend" class="text-primary text-decoration-none fw-bold">
                        Gửi lại mã OTP
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
