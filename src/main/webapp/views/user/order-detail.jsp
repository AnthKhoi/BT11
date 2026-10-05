<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chi tiết đơn hàng #OD-${order.order_id} - BookStore 24133034</title>
    <style>
        .detail-card {
            background: #fff;
            border: 1px solid #e2e8f0;
            border-radius: 10px;
            padding: 24px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.04);
        }
        .order-book-img {
            width: 70px;
            height: 95px;
            object-fit: cover;
            border-radius: 4px;
            border: 1px solid #e2e8f0;
        }
    </style>
</head>
<body>

<div class="container my-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none"><i class="bi bi-house-door"></i> Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/order-history" class="text-decoration-none">Lịch sử đặt hàng</a></li>
            <li class="breadcrumb-item active" aria-current="page">Chi tiết đơn #OD-${order.order_id}</li>
        </ol>
    </nav>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h3 class="fw-bold mb-1">
                Chi tiết đơn hàng <span class="text-primary">#OD-${order.order_id}</span>
            </h3>
            <span class="text-muted small">
                Ngày đặt: <fmt:formatDate value="${order.created_date}" pattern="dd/MM/yyyy HH:mm:ss" />
            </span>
        </div>
        <div>
            <span class="badge ${order.statusBadgeClass} fs-6 px-3 py-2">
                <i class="bi ${order.statusIcon} me-1"></i> ${order.status}
            </span>
        </div>
    </div>

    <div class="row g-4 mb-4">
        <!-- Thông tin giao hàng -->
        <div class="col-md-6">
            <div class="detail-card h-100">
                <h5 class="fw-bold border-bottom pb-2 mb-3">
                    <i class="bi bi-person-lines-fill text-primary me-2"></i> Thông tin người nhận
                </h5>
                <p class="mb-2"><strong>Người nhận:</strong> ${order.receiver_name}</p>
                <p class="mb-2"><strong>Số điện thoại:</strong> ${order.receiver_phone}</p>
                <p class="mb-2"><strong>Địa chỉ giao hàng:</strong> ${order.receiver_address}</p>
                <c:if test="${not empty order.note}">
                    <p class="mb-0"><strong>Ghi chú đơn hàng:</strong> <em>"${order.note}"</em></p>
                </c:if>
            </div>
        </div>

        <!-- Phương thức thanh toán -->
        <div class="col-md-6">
            <div class="detail-card h-100">
                <h5 class="fw-bold border-bottom pb-2 mb-3">
                    <i class="bi bi-credit-card-2-front text-success me-2"></i> Thanh toán & Vận chuyển
                </h5>
                <p class="mb-2"><strong>Phương thức thanh toán:</strong> 
                    <span class="badge bg-warning text-dark"><i class="bi bi-cash-stack"></i> COD - Thanh toán khi nhận hàng</span>
                </p>
                <p class="mb-2"><strong>Trạng thái thanh toán:</strong> 
                    <c:choose>
                        <c:when test="${order.status == 'Đã giao'}">
                            <span class="badge bg-success">Đã thanh toán cho Shipper</span>
                        </c:when>
                        <c:when test="${order.status == 'Đơn hàng hủy'}">
                            <span class="badge bg-danger">Đã hủy</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-secondary">Chờ thanh toán khi nhận hàng</span>
                        </c:otherwise>
                    </c:choose>
                </p>
                <p class="mb-0"><strong>Phí vận chuyển:</strong> <span class="text-success fw-bold">Miễn phí (0 ₫)</span></p>
            </div>
        </div>
    </div>

    <!-- Bảng danh sách sản phẩm trong đơn -->
    <div class="detail-card mb-4">
        <h5 class="fw-bold border-bottom pb-2 mb-3">
            <i class="bi bi-box-seam text-info me-2"></i> Danh sách sách trong đơn (${order.totalQuantity} cuốn)
        </h5>

        <div class="table-responsive">
            <table class="table align-middle">
                <thead class="table-light">
                    <tr>
                        <th style="min-width: 250px;">Sách</th>
                        <th class="text-center">Mã ISBN</th>
                        <th class="text-center">Đơn giá</th>
                        <th class="text-center">Số lượng</th>
                        <th class="text-end">Thành tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${order.items}">
                        <tr>
                            <td>
                                <div class="d-flex align-items-center">
                                    <a href="${pageContext.request.contextPath}/book-detail?id=${item.bookid}">
                                        <c:choose>
                                            <c:when test="${not empty item.book.cover_image}">
                                                <c:choose>
                                                    <c:when test="${item.book.cover_image.startsWith('http://') or item.book.cover_image.startsWith('https://')}">
                                                        <img src="${item.book.cover_image}" alt="${item.book.title}" class="order-book-img me-3">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <img src="${pageContext.request.contextPath}/uploads/${item.book.cover_image}"
                                                             onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/uploads/default-book.png'"
                                                             alt="${item.book.title}" class="order-book-img me-3">
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/uploads/default-book.png" alt="${item.book.title}" class="order-book-img me-3">
                                            </c:otherwise>
                                        </c:choose>
                                    </a>
                                    <div>
                                        <a href="${pageContext.request.contextPath}/book-detail?id=${item.bookid}" class="fw-bold text-dark text-decoration-none">
                                            ${item.book.title}
                                        </a>
                                        <div class="text-muted small">Nhà XB: ${item.book.publisher}</div>
                                    </div>
                                </div>
                            </td>
                            <td class="text-center text-muted">${item.book.isbn}</td>
                            <td class="text-center fw-semibold">
                                <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </td>
                            <td class="text-center fw-bold">x${item.quantity}</td>
                            <td class="text-end fw-bold text-danger">
                                <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="4" class="text-end fw-bold fs-5">Tổng tiền thanh toán (COD):</td>
                        <td class="text-end fw-bold text-danger fs-4">
                            <fmt:formatNumber value="${order.total_money}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>

    <!-- Nút hành động -->
    <div class="d-flex justify-content-between">
        <a href="${pageContext.request.contextPath}/order-history" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left"></i> Quay lại lịch sử đơn hàng
        </a>

        <c:if test="${order.canCancel}">
            <form action="${pageContext.request.contextPath}/order-cancel" method="post"
                  onsubmit="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này?');">
                <input type="hidden" name="orderId" value="${order.order_id}">
                <button type="submit" class="btn btn-danger">
                    <i class="bi bi-x-circle"></i> Hủy đơn hàng này
                </button>
            </form>
        </c:if>
    </div>

</div>

</body>
</html>
