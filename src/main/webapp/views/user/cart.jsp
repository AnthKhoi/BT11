<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Giỏ hàng của bạn - BookStore 24133034</title>
    <style>
        .cart-table th {
            background-color: #f1f5f9;
            font-weight: 600;
            color: #334155;
        }
        .cart-img {
            width: 80px;
            height: 110px;
            object-fit: cover;
            border-radius: 6px;
            border: 1px solid #e2e8f0;
        }
        .qty-input {
            width: 60px;
            text-align: center;
            font-weight: 600;
        }
        .summary-card {
            background: #ffffff;
            border: 1px solid #e2e8f0;
            border-radius: 10px;
            padding: 24px;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
        }
        .stock-badge {
            font-size: 12px;
        }
    </style>
</head>
<body>

<div class="container my-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none"><i class="bi bi-house-door"></i> Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Giỏ hàng</li>
        </ol>
    </nav>

    <!-- Flash Messages -->
    <c:if test="${not empty sessionScope.cart_success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.cart_success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="cart_success" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.cart_warning}">
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${sessionScope.cart_warning}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="cart_warning" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.cart_error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-x-circle-fill me-2"></i> ${sessionScope.cart_error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="cart_error" scope="session" />
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fw-bold text-dark mb-0">
            <i class="bi bi-cart3 text-primary"></i> Giỏ hàng của bạn
            <span class="fs-6 text-muted fw-normal">(${cart != null ? cart.totalQuantity : 0} sản phẩm)</span>
        </h3>
        <c:if test="${not empty cart and not empty cart.items}">
            <a href="${pageContext.request.contextPath}/cart/clear" 
               class="btn btn-outline-danger btn-sm"
               onclick="return confirm('Bạn có chắc chắn muốn xóa toàn bộ sản phẩm khỏi giỏ hàng?');">
                <i class="bi bi-trash"></i> Xóa tất cả
            </a>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${not empty cart and not empty cart.items}">
            <div class="row g-4">
                <!-- Danh sách sản phẩm trong giỏ -->
                <div class="col-lg-8">
                    <div class="card shadow-sm border-0 rounded-3 overflow-hidden">
                        <div class="table-responsive">
                            <table class="table cart-table align-middle mb-0">
                                <thead>
                                    <tr>
                                        <th scope="col" style="min-width: 260px;">Sách</th>
                                        <th scope="col" class="text-center">Đơn giá</th>
                                        <th scope="col" class="text-center" style="min-width: 170px;">Số lượng</th>
                                        <th scope="col" class="text-end">Thành tiền</th>
                                        <th scope="col" class="text-center" style="width: 50px;">Xóa</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${cart.items}">
                                        <tr>
                                            <!-- Sách -->
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <a href="${pageContext.request.contextPath}/book-detail?id=${item.book.bookid}">
                                                        <c:choose>
                                                            <c:when test="${not empty item.book.cover_image}">
                                                                <c:choose>
                                                                    <c:when test="${item.book.cover_image.startsWith('http://') or item.book.cover_image.startsWith('https://')}">
                                                                        <img src="${item.book.cover_image}" alt="${item.book.title}" class="cart-img me-3">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img src="${pageContext.request.contextPath}/uploads/${item.book.cover_image}"
                                                                             onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/uploads/default-book.png'"
                                                                             alt="${item.book.title}" class="cart-img me-3">
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <img src="${pageContext.request.contextPath}/uploads/default-book.png" alt="${item.book.title}" class="cart-img me-3">
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </a>
                                                    <div>
                                                        <a href="${pageContext.request.contextPath}/book-detail?id=${item.book.bookid}" class="text-dark fw-bold text-decoration-none">
                                                            ${item.book.title}
                                                        </a>
                                                        <div class="text-muted small mt-1">
                                                            <c:if test="${not empty item.book.author_name}">
                                                                Tác giả: ${item.book.author_name}
                                                            </c:if>
                                                        </div>
                                                        <div class="mt-1">
                                                            <span class="badge bg-light text-secondary border stock-badge">
                                                                <i class="bi bi-box-seam"></i> Còn lại: <strong>${item.book.quantity}</strong> cuốn
                                                            </span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>

                                            <!-- Đơn giá -->
                                            <td class="text-center text-primary fw-semibold">
                                                <fmt:formatNumber value="${item.book.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                            </td>

                                            <!-- Số lượng với nút +/- và ô nhập giới hạn trong số lượng tồn kho -->
                                            <td class="text-center">
                                                <form action="${pageContext.request.contextPath}/cart/update" method="post" class="d-inline-flex align-items-center justify-content-center">
                                                    <input type="hidden" name="bookId" value="${item.book.bookid}">
                                                    
                                                    <!-- Nút giảm -->
                                                    <button type="submit" name="operation" value="dec" class="btn btn-outline-secondary btn-sm px-2" title="Giảm số lượng">
                                                        <i class="bi bi-dash"></i>
                                                    </button>
                                                    
                                                    <!-- Ô nhập số lượng (tối thiểu 1, tối đa bằng tồn kho) -->
                                                    <input type="number" name="quantity" value="${item.quantity}" 
                                                           min="1" max="${item.book.quantity}" 
                                                           class="form-control form-control-sm qty-input mx-1" 
                                                           onchange="this.form.submit()"
                                                           title="Nhập số lượng từ 1 đến ${item.book.quantity}">
                                                    
                                                    <!-- Nút tăng -->
                                                    <button type="submit" name="operation" value="inc" class="btn btn-outline-secondary btn-sm px-2" 
                                                            ${item.quantity >= item.book.quantity ? 'disabled' : ''} 
                                                            title="Tăng số lượng">
                                                        <i class="bi bi-plus"></i>
                                                    </button>
                                                </form>
                                                <c:if test="${item.quantity >= item.book.quantity}">
                                                    <div class="text-danger small mt-1" style="font-size: 11px;">Đã đạt tối đa kho</div>
                                                </c:if>
                                            </td>

                                            <!-- Thành tiền -->
                                            <td class="text-end fw-bold text-danger">
                                                <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                            </td>

                                            <!-- Nút xóa -->
                                            <td class="text-center">
                                                <a href="${pageContext.request.contextPath}/cart/remove?bookId=${item.book.bookid}" 
                                                   class="btn btn-sm btn-outline-danger border-0" 
                                                   title="Xóa sản phẩm này"
                                                   onclick="return confirm('Xóa cuốn sách này khỏi giỏ hàng?');">
                                                    <i class="bi bi-trash-fill fs-5"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <!-- Card footer quay lại mua hàng -->
                        <div class="card-footer bg-white p-3 d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-primary">
                                <i class="bi bi-arrow-left"></i> Tiếp tục chọn sách
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Tóm tắt đơn hàng & Nút thanh toán -->
                <div class="col-lg-4">
                    <div class="summary-card">
                        <h5 class="fw-bold border-bottom pb-3 mb-3 text-dark">
                            <i class="bi bi-receipt"></i> Tóm tắt đơn hàng
                        </h5>
                        
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Tổng số lượng:</span>
                            <span class="fw-semibold">${cart.totalQuantity} cuốn</span>
                        </div>
                        
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Tạm tính:</span>
                            <span class="fw-semibold">
                                <fmt:formatNumber value="${cart.totalMoney}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </span>
                        </div>

                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Phí vận chuyển:</span>
                            <span class="text-success fw-semibold"><i class="bi bi-gift"></i> Miễn phí</span>
                        </div>

                        <div class="d-flex justify-content-between mb-3">
                            <span class="text-muted">Hình thức thanh toán:</span>
                            <span class="badge bg-warning text-dark"><i class="bi bi-cash-stack"></i> COD (Nhận hàng)</span>
                        </div>

                        <hr>

                        <div class="d-flex justify-content-between align-items-center mb-4">
                            <span class="fs-5 fw-bold text-dark">Tổng thanh toán:</span>
                            <span class="fs-4 fw-bold text-danger">
                                <fmt:formatNumber value="${cart.totalMoney}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </span>
                        </div>

                        <!-- Nút tiến hành thanh toán -->
                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-lg w-100 fw-bold shadow-sm py-3 mb-2">
                            <i class="bi bi-credit-card-2-front"></i> Tiến hành thanh toán COD
                        </a>

                        <div class="text-center text-muted small mt-3">
                            <i class="bi bi-shield-check text-success"></i> Cam kết đóng gói cẩn thận, giao hàng tận nơi
                        </div>
                    </div>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="card border-0 shadow-sm rounded-4 p-5 text-center my-4 bg-white">
                <div class="my-4">
                    <i class="bi bi-cart-x text-muted" style="font-size: 80px;"></i>
                    <h4 class="fw-bold mt-3 text-secondary">Giỏ hàng của bạn đang trống!</h4>
                    <p class="text-muted mb-4">Bạn chưa chọn cuốn sách nào vào giỏ hàng. Hãy khám phá kho sách đa dạng của chúng tôi nhé!</p>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary btn-lg px-4 fw-bold">
                        <i class="bi bi-book"></i> Khám phá mua sách ngay
                    </a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

</div>

</body>
</html>
