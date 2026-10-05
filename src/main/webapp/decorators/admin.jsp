<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:title default="Admin Panel - BookStore 24133034" /></title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            background-color: #f4f6f9;
        }
        .admin-sidebar {
            background-color: #343a40;
            min-height: calc(100vh - 120px);
            color: #c2c7d0;
        }
        .admin-sidebar .nav-link {
            color: #c2c7d0;
            border-radius: 4px;
            margin-bottom: 4px;
            padding: 10px 15px;
        }
        .admin-sidebar .nav-link:hover, .admin-sidebar .nav-link.active {
            background-color: #0d6efd;
            color: #ffffff;
        }
        .admin-content {
            flex: 1;
            padding: 25px;
        }
        .footer-box {
            background-color: #212529;
            color: #ffffff;
            padding: 15px 0;
            margin-top: auto;
        }
    </style>
    <sitemesh:head />
</head>
<body>

    <!-- Admin Top Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top border-bottom border-secondary">
        <div class="container-fluid">
            <a class="navbar-brand text-warning fw-bold" href="${pageContext.request.contextPath}/admin/home">
                <i class="bi bi-shield-shaded"></i> ADMIN PANEL - 24133034
            </a>
            <div class="ms-auto d-flex align-items-center">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-info btn-sm me-3" target="_blank">
                    <i class="bi bi-box-arrow-up-right"></i> Xem Trang Khách
                </a>
                <span class="text-white me-3">
                    <i class="bi bi-person-badge"></i> ${sessionScope.account.fullname} (Admin)
                </span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">
                    <i class="bi bi-power"></i> Đăng xuất
                </a>
            </div>
        </div>
    </nav>

    <!-- Admin Body with Sidebar -->
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block admin-sidebar p-3">
                <div class="position-sticky">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/home">
                                <i class="bi bi-speedometer2 me-2"></i> Tổng Quan
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/books">
                                <i class="bi bi-book me-2"></i> Quản Lý Sách (CRUD)
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/book/create">
                                <i class="bi bi-plus-circle me-2"></i> Thêm Sách Mới
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/orders">
                                <i class="bi bi-cart-check me-2"></i> Quản Lý Đơn Hàng
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main Content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 admin-content">
                <sitemesh:body />
            </main>
        </div>
    </div>

    <!-- Footer theo yêu cầu đề thi Câu 1 -->
    <footer class="footer-box text-center">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-4 text-md-start">
                    <p class="mb-0"><strong>Họ tên:</strong> Lê Huỳnh Anh Khôi</p>
                </div>
                <div class="col-md-4 text-center">
                    <p class="mb-0"><strong>MSSV:</strong> 24133034</p>
                </div>
                <div class="col-md-4 text-md-end">
                    <p class="mb-0"><strong>Mã đề:</strong> Đề số 02</p>
                </div>
            </div>
        </div>
    </footer>

    <!-- Bootstrap 5 Bundle JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
