<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thanh toán đơn hàng COD - BookStore 24133034</title>
    <style>
        .checkout-box {
            background: #ffffff;
            border: 1px solid #e2e8f0;
            border-radius: 10px;
            padding: 24px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.04);
        }
        .cod-card {
            border: 2px solid #0d6efd;
            background-color: #f0f7ff;
            border-radius: 8px;
            padding: 16px;
        }
        .checkout-item-img {
            width: 50px;
            height: 70px;
            object-fit: cover;
            border-radius: 4px;
        }
    </style>
</head>
<body>

<div class="container my-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none"><i class="bi bi-house-door"></i> Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/cart" class="text-decoration-none">Giỏ hàng</a></li>
            <li class="breadcrumb-item active" aria-current="page">Thanh toán COD</li>
        </ol>
    </nav>

    <h3 class="fw-bold mb-4 text-dark">
        <i class="bi bi-bag-check-fill text-primary"></i> Thanh toán đơn hàng
    </h3>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/checkout" method="post">
        <div class="row g-4">
            
            <!-- Cột trái: Thông tin giao hàng & Phương thức thanh toán -->
            <div class="col-lg-7">
                <div class="checkout-box mb-4">
                    <h5 class="fw-bold mb-3 text-dark border-bottom pb-2">
                        <i class="bi bi-geo-alt-fill text-danger me-2"></i> 1. Thông tin giao hàng
                    </h5>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Họ và tên người nhận <span class="text-danger">*</span></label>
                        <input type="text" name="receiver_name" class="form-control" 
                               value="${empty user.fullname ? '' : user.fullname}" 
                               placeholder="Ví dụ: Nguyễn Văn A" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Số điện thoại nhận hàng <span class="text-danger">*</span></label>
                        <input type="tel" name="receiver_phone" class="form-control" 
                               value="${user.formattedPhone}" 
                               placeholder="Ví dụ: 0912345678" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Địa chỉ giao hàng chi tiết <span class="text-danger">*</span></label>
                        <textarea name="receiver_address" rows="3" class="form-control" 
                                  placeholder="Số nhà, tên đường, phường/xã, quận/huyện, tỉnh/thành phố..." required></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Ghi chú đơn hàng (nếu có)</label>
                        <textarea name="note" rows="2" class="form-control" 
                                  placeholder="Ví dụ: Giao giờ hành chính, gọi trước khi giao..."></textarea>
                    </div>
                </div>

                <!-- Phương thức thanh toán COD -->
                <div class="checkout-box">
                    <h5 class="fw-bold mb-3 text-dark border-bottom pb-2">
                        <i class="bi bi-cash-stack text-success me-2"></i> 2. Phương thức thanh toán
                    </h5>

                    <div class="cod-card">
                        <div class="form-check d-flex align-items-center">
                            <input class="form-check-input me-3" type="radio" name="payment_method" id="paymentCOD" value="COD" checked>
                            <label class="form-check-label w-100" for="paymentCOD">
                                <div class="d-flex justify-content-between align-items-center">
                                    <strong class="fs-6 text-primary">Thanh toán khi nhận hàng (COD)</strong>
                                    <span class="badge bg-success">Khuyên dùng</span>
                                </div>
                                <div class="text-muted small mt-1">
                                    Bạn chỉ cần thanh toán tiền mặt trực tiếp cho nhân viên giao hàng (Shipper) khi bưu kiện được giao đến tận tay.
                                </div>
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Cột phải: Tóm tắt đơn hàng -->
            <div class="col-lg-5">
                <div class="checkout-box">
                    <h5 class="fw-bold mb-3 text-dark border-bottom pb-2">
                        <i class="bi bi-card-checklist me-2"></i> Đơn hàng của bạn (${cart.totalQuantity} cuốn)
                    </h5>

                    <!-- Danh sách các sách tóm tắt -->
                    <div class="mb-3" style="max-height: 280px; overflow-y: auto;">
                        <c:forEach var="item" items="${cart.items}">
                            <div class="d-flex align-items-center py-2 border-bottom">
                                <c:choose>
                                    <c:when test="${not empty item.book.cover_image}">
                                        <c:choose>
                                            <c:when test="${item.book.cover_image.startsWith('http://') or item.book.cover_image.startsWith('https://')}">
                                                <img src="${item.book.cover_image}" alt="${item.book.title}" class="checkout-item-img me-3">
                                            </c:when>
                                            <c:otherwise>
                                                 <img src="${pageContext.request.contextPath}/uploads/${item.book.cover_image}"
                                                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/uploads/default-book.png'"
                                                     alt="${item.book.title}" class="checkout-item-img me-3">
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/uploads/default-book.png" alt="${item.book.title}" class="checkout-item-img me-3">
                                    </c:otherwise>
                                </c:choose>
                                <div class="flex-grow-1">
                                    <div class="fw-bold text-dark small text-truncate" style="max-width: 200px;">
                                        ${item.book.title}
                                    </div>
                                    <div class="text-muted small">
                                        Số lượng: <strong>x${item.quantity}</strong>
                                    </div>
                                </div>
                                <div class="text-end fw-semibold text-danger small">
                                    <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Tiền tính toán -->
                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Tạm tính:</span>
                        <span class="fw-semibold">
                            <fmt:formatNumber value="${cart.totalMoney}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </span>
                    </div>

                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Phí giao hàng:</span>
                        <span class="text-success fw-semibold"><i class="bi bi-gift-fill"></i> Miễn phí (0 ₫)</span>
                    </div>

                    <hr>

                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <span class="fs-5 fw-bold text-dark">Tổng thanh toán:</span>
                        <span class="fs-4 fw-bold text-danger">
                            <fmt:formatNumber value="${cart.totalMoney}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </span>
                    </div>

                    <!-- Nút xác nhận đặt hàng -->
                    <button type="submit" class="btn btn-primary btn-lg w-100 fw-bold shadow-sm py-3 mb-2">
                        <i class="bi bi-check2-circle me-1"></i> Xác nhận đặt hàng COD
                    </button>

                    <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-secondary w-100 mt-2">
                        <i class="bi bi-arrow-left"></i> Quay lại kiểm tra giỏ hàng
                    </a>

                    <div class="alert alert-info small mt-3 mb-0">
                        <i class="bi bi-info-circle-fill"></i> Đơn hàng sau khi đặt sẽ ở trạng thái <strong>"Đơn hàng mới"</strong> và bạn có thể theo dõi trong mục <em>Lịch sử đặt hàng</em>.
                    </div>
                </div>
            </div>

        </div>
    </form>

</div>

</body>
</html>
