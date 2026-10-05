<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập - BookStore</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow-sm border-0 mt-4">
            <div class="card-header bg-primary text-white text-center py-3">
                <h4 class="mb-0"><i class="bi bi-box-arrow-in-right"></i> ĐĂNG NHẬP</h4>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        <i class="bi bi-exclamation-triangle-fill"></i> ${errorMessage}
                    </div>
                </c:if>
                <c:if test="${param.message == 'activated'}">
                    <div class="alert alert-success" role="alert">
                        <i class="bi bi-check-circle-fill"></i> Kích hoạt tài khoản thành công! Vui lòng đăng nhập.
                    </div>
                </c:if>
                <c:if test="${param.message == 'logged_out'}">
                    <div class="alert alert-info" role="alert">
                        <i class="bi bi-info-circle-fill"></i> Bạn đã đăng xuất thành công.
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label class="form-label fw-bold">Email:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                            <input type="email" name="email" class="form-control" placeholder="nhapemail@gmail.com" required autofocus>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold">Mật khẩu:</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-key"></i></span>
                            <input type="password" name="password" class="form-control" placeholder="Nhập mật khẩu" required>
                        </div>
                    </div>

                    <div class="d-grid mt-4">
                        <button type="submit" class="btn btn-primary btn-block">Đăng Nhập</button>
                    </div>
                </form>

                <div class="text-center mt-3">
                    <span>Chưa có tài khoản? </span>
                    <a href="${pageContext.request.contextPath}/register" class="text-primary text-decoration-none fw-bold">Đăng ký ngay</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
