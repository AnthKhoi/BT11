<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bảng Điều Khiển Quản Trị - BookStore</title>
</head>
<body>
<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="h3 fw-bold text-gray-800">Trang Quản Trị Hệ Thống</h2>
        <a href="${pageContext.request.contextPath}/admin/book/create" class="btn btn-primary">
            <i class="bi bi-plus-lg"></i> Thêm Sách Mới
        </a>
    </div>

    <!-- Thống kê -->
    <div class="row g-4 mb-4">
        <div class="col-md-4">
            <div class="card bg-primary text-white shadow-sm border-0">
                <div class="card-body d-flex justify-content-between align-items-center py-4">
                    <div>
                        <h6 class="text-uppercase mb-1">Tổng Số Đầu Sách</h6>
                        <h2 class="display-6 fw-bold mb-0">${totalBooks}</h2>
                    </div>
                    <i class="bi bi-book-half fs-1 opacity-75"></i>
                </div>
                <div class="card-footer bg-primary bg-opacity-75 border-0">
                    <a href="${pageContext.request.contextPath}/admin/books" class="text-white text-decoration-none small">
                        Xem chi tiết & quản lý <i class="bi bi-arrow-right"></i>
                    </a>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card bg-success text-white shadow-sm border-0">
                <div class="card-body d-flex justify-content-between align-items-center py-4">
                    <div>
                        <h6 class="text-uppercase mb-1">Tổng Số Tác Giả</h6>
                        <h2 class="display-6 fw-bold mb-0">${totalAuthors}</h2>
                    </div>
                    <i class="bi bi-people-fill fs-1 opacity-75"></i>
                </div>
                <div class="card-footer bg-success bg-opacity-75 border-0">
                    <span class="small">Dữ liệu tác giả trong hệ thống</span>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card bg-info text-white shadow-sm border-0">
                <div class="card-body d-flex justify-content-between align-items-center py-4">
                    <div>
                        <h6 class="text-uppercase mb-1">Tổng Người Dùng</h6>
                        <h2 class="display-6 fw-bold mb-0">${totalUsers}</h2>
                    </div>
                    <i class="bi bi-person-check-fill fs-1 opacity-75"></i>
                </div>
                <div class="card-footer bg-info bg-opacity-75 border-0">
                    <span class="small">Tài khoản khách hàng và quản trị</span>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
