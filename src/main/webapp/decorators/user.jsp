<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:title default="BookStore 24133034" /></title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            background-color: #f8f9fa;
        }
        .main-content {
            flex: 1;
            padding: 25px 0;
        }
        .navbar-brand {
            font-weight: 700;
            color: #0d6efd !important;
        }
        .footer-box {
            background-color: #212529;
            color: #ffffff;
            padding: 20px 0;
            margin-top: auto;
        }
        .book-card {
            background: #ffffff;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            transition: box-shadow 0.2s ease-in-out;
        }
        .book-card:hover {
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        .book-cover-img {
            width: 100%;
            height: 250px;
            object-fit: cover;
            border-radius: 4px;
            background-color: #e9ecef;
        }
        .page-link {
            color: #0d6efd;
        }
        .page-item.active .page-link {
            background-color: #0d6efd;
            border-color: #0d6efd;
            color: #ffffff;
        }
    </style>
    <sitemesh:head />
</head>
<body>

    <!-- Header Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm">
        <div class="container">
            <a class="navbar-brand text-primary" href="${pageContext.request.contextPath}/home">
                <i class="bi bi-book-half"></i> BookStore 24133034
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-nav-item">
                        <a class="nav-link text-white" href="${pageContext.request.contextPath}/home">
                            <i class="bi bi-house-door"></i> Trang Chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-white" href="${pageContext.request.contextPath}/home">
                            <i class="bi bi-grid"></i> Sản phẩm
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link text-white position-relative" href="${pageContext.request.contextPath}/cart">
                            <i class="bi bi-cart3"></i> Giỏ hàng
                            <c:if test="${not empty sessionScope.cart and sessionScope.cart.totalQuantity > 0}">
                                <span class="badge bg-danger rounded-pill ms-1">${sessionScope.cart.totalQuantity}</span>
                            </c:if>
                        </a>
                    </li>
                    <c:if test="${not empty sessionScope.account}">
                        <li class="nav-item">
                            <a class="nav-link text-white" href="${pageContext.request.contextPath}/order-history">
                                <i class="bi bi-clock-history"></i> Lịch sử đơn hàng
                            </a>
                        </li>
                    </c:if>
                    <!-- Trang quản trị (Chỉ admin mới có chức năng này) -->
                    <c:if test="${not empty sessionScope.account and sessionScope.account.is_admin}">
                        <li class="nav-item">
                            <a class="nav-link text-warning fw-bold" href="${pageContext.request.contextPath}/admin/home">
                                <i class="bi bi-shield-lock"></i> Trang quản trị
                            </a>
                        </li>
                    </c:if>
                </ul>

                <!-- User Session Menu -->
                <ul class="navbar-nav ms-auto">
                    <c:choose>
                        <c:when test="${not empty sessionScope.account}">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle text-white" href="#" id="userDrop" role="button" data-bs-toggle="dropdown">
                                    <i class="bi bi-person-circle"></i> Xin chào, ${sessionScope.account.fullname}
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end shadow">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/order-history"><i class="bi bi-clock-history"></i> Lịch sử đơn hàng</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/cart"><i class="bi bi-cart3"></i> Giỏ hàng của tôi</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <c:if test="${sessionScope.account.is_admin}">
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/books"><i class="bi bi-gear"></i> Quản lý sách</a></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/orders"><i class="bi bi-cart-check"></i> Quản lý đơn hàng</a></li>
                                        <li><hr class="dropdown-divider"></li>
                                    </c:if>
                                    <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout"><i class="bi bi-box-arrow-right"></i> Đăng xuất</a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link text-white" href="${pageContext.request.contextPath}/login">
                                    <i class="bi bi-box-arrow-in-right"></i> Đăng nhập
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-outline-primary btn-sm ms-2 my-1" href="${pageContext.request.contextPath}/register">
                                    Đăng ký
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Body Content -->
    <main class="main-content">
        <div class="container">
            <sitemesh:body />
        </div>
    </main>

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
