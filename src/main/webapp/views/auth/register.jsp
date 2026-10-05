<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký tài khoản - BookStore</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card shadow-sm border-0 mt-3 mb-4">
            <div class="card-header bg-success text-white text-center py-3">
                <h4 class="mb-0"><i class="bi bi-person-plus-fill"></i> ĐĂNG KÝ TÀI KHOẢN</h4>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        <i class="bi bi-exclamation-triangle-fill"></i> ${errorMessage}
                    </div>
                </c:if>

                <p class="text-muted text-center small mb-4">
                    Sau khi đăng ký, hệ thống sẽ gửi một mã <strong>OTP kích hoạt</strong> tới email của bạn.
                </p>

                <form action="${pageContext.request.contextPath}/register" method="post">
                    <div class="mb-3">
                        <label class="form-label fw-bold">Họ và tên:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-person"></i></span>
                            <input type="text" name="fullname" class="form-control" placeholder="Nguyễn Văn A" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">Email:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                            <input type="email" name="email" class="form-control" placeholder="example@gmail.com" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">Số điện thoại:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-telephone"></i></span>
                            <input type="text" name="phone" class="form-control" placeholder="0901234567" pattern="[0-9]{9,11}">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">Mật khẩu:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-lock"></i></span>
                            <input type="password" name="password" class="form-control" placeholder="Nhập mật khẩu" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">Xác nhận mật khẩu:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-check-circle"></i></span>
                            <input type="password" name="confirmPassword" class="form-control" placeholder="Nhập lại mật khẩu" required>
                        </div>
                    </div>

                    <div class="d-grid mt-4">
                        <button type="submit" class="btn btn-success btn-block">Đăng Ký & Nhận OTP</button>
                    </div>
                </form>

                <div class="text-center mt-3">
                    <span>Đã có tài khoản? </span>
                    <a href="${pageContext.request.contextPath}/login" class="text-success text-decoration-none fw-bold">Đăng nhập</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
