<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lịch sử đơn hàng - BookStore 24133034</title>
    <style>
        .nav-tabs .nav-link {
            color: #495057;
            font-weight: 500;
            padding: 12px 16px;
            border-radius: 8px 8px 0 0;
            white-space: nowrap;
        }
        .nav-tabs .nav-link.active {
            color: #0d6efd;
            font-weight: 700;
            border-bottom: 3px solid #0d6efd;
            background-color: #fff;
        }
        .order-card {
            background: #fff;
            border: 1px solid #e2e8f0;
            border-radius: 10px;
            margin-bottom: 24px;
            overflow: hidden;
            box-shadow: 0 2px 5px rgba(0,0,0,0.04);
            transition: all 0.2s ease-in-out;
        }
        .order-card:hover {
            box-shadow: 0 6px 16px rgba(0,0,0,0.08);
        }
        .order-header {
            background-color: #f8fafc;
            border-bottom: 1px solid #e2e8f0;
            padding: 14px 20px;
        }
        .order-book-img {
            width: 65px;
            height: 90px;
            object-fit: cover;
            border-radius: 4px;
            border: 1px solid #e2e8f0;
        }
        .status-badge {
            font-size: 13px;
            padding: 6px 12px;
            border-radius: 20px;
            font-weight: 600;
        }
        /* Custom mini timeline */
        .order-stepper {
            display: flex;
            justify-content: space-between;
            position: relative;
            padding: 15px 10px;
            margin-bottom: 15px;
            background: #fdfdfd;
            border-radius: 8px;
            border: 1px dashed #cbd5e1;
        }
        .step-item {
            text-align: center;
            flex: 1;
            font-size: 12px;
            color: #94a3b8;
            font-weight: 500;
            position: relative;
        }
        .step-item.active {
            color: #0d6efd;
            font-weight: 700;
        }
        .step-item.completed {
            color: #198754;
            font-weight: 600;
        }
        .step-dot {
            width: 14px;
            height: 14px;
            border-radius: 50%;
            background-color: #cbd5e1;
            margin: 0 auto 6px auto;
        }
        .step-item.active .step-dot {
            background-color: #0d6efd;
            box-shadow: 0 0 0 3px rgba(13, 110, 253, 0.25);
        }
        .step-item.completed .step-dot {
            background-color: #198754;
        }
    </style>
</head>
<body>

<div class="container my-4">

    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none"><i class="bi bi-house-door"></i> Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Lịch sử đặt hàng</li>
        </ol>
    </nav>

    <!-- Header Tiêu đề -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fw-bold text-dark mb-0">
            <i class="bi bi-clock-history text-primary"></i> Lịch sử đơn hàng của tôi
        </h3>
        <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-primary btn-sm">
            <i class="bi bi-plus-circle"></i> Tiếp tục mua sách
        </a>
    </div>

    <!-- Thông báo kết quả thao tác -->
    <c:if test="${not empty sessionScope.order_success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.order_success}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="order_success" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.order_error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-circle-fill me-2"></i> ${sessionScope.order_error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="order_error" scope="session" />
    </c:if>


    <!-- Thanh Tabs lọc theo 8 trạng thái -->
    <div class="table-responsive mb-4">
        <ul class="nav nav-tabs flex-nowrap border-bottom" role="tablist">
            <!-- Tab Tất cả -->
            <li class="nav-item">
                <a class="nav-link ${currentStatus == 'all' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/order-history?status=all">
                    Tất cả <span class="badge bg-secondary rounded-pill ms-1">${statusCounts['all']}</span>
                </a>
            </li>

            <!-- 1. Đơn hàng mới -->
            <li class="nav-item">
                <a class="nav-link ${currentStatus == 'Đơn hàng mới' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/order-history?status=Đơn hàng mới">
                    <i class="bi bi-receipt"></i> Đơn hàng mới 
                    <span class="badge bg-primary rounded-pill ms-1">${statusCounts['Đơn hàng mới']}</span>
                </a>
            </li>

            <!-- 2. Đã xác nhận -->
            <li class="nav-item">
                <a class="nav-link ${currentStatus == 'Đã xác nhận' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/order-history?status=Đã xác nhận">
                    <i class="bi bi-check2-circle"></i> Đã xác nhận 
                    <span class="badge bg-info text-dark rounded-pill ms-1">${statusCounts['Đã xác nhận']}</span>
                </a>
            </li>

            <!-- 3. Chuẩn bị hàng -->
            <li class="nav-item">
                <a class="nav-link ${currentStatus == 'Chuẩn bị hàng' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/order-history?status=Chuẩn bị hàng">
                    <i class="bi bi-box-seam"></i> Chuẩn bị hàng 
                    <span class="badge bg-warning text-dark rounded-pill ms-1">${statusCounts['Chuẩn bị hàng']}</span>
                </a>
            </li>

            <!-- 4. Vận chuyển -->
            <li class="nav-item">
                <a class="nav-link ${currentStatus == 'Vận chuyển' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/order-history?status=Vận chuyển">
                    <i class="bi bi-truck"></i> Vận chuyển 
                    <span class="badge bg-secondary rounded-pill ms-1">${statusCounts['Vận chuyển']}</span>
                </a>
            </li>

            <!-- 5. Giao hàng -->
            <li class="nav-item">
                <a class="nav-link ${currentStatus == 'Giao hàng' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/order-history?status=Giao hàng">
                    <i class="bi bi-bicycle"></i> Giao hàng 
                    <span class="badge bg-info rounded-pill ms-1">${statusCounts['Giao hàng']}</span>
                </a>
            </li>

            <!-- 6. Đã giao -->
            <li class="nav-item">
                <a class="nav-link ${currentStatus == 'Đã giao' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/order-history?status=Đã giao">
                    <i class="bi bi-patch-check-fill"></i> Đã giao 
                    <span class="badge bg-success rounded-pill ms-1">${statusCounts['Đã giao']}</span>
                </a>
            </li>

            <!-- 7. Đơn hàng hủy -->
            <li class="nav-item">
                <a class="nav-link ${currentStatus == 'Đơn hàng hủy' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/order-history?status=Đơn hàng hủy">
                    <i class="bi bi-x-circle-fill"></i> Đơn hàng hủy 
                    <span class="badge bg-danger rounded-pill ms-1">${statusCounts['Đơn hàng hủy']}</span>
                </a>
            </li>

            <!-- 8. Đơn hàng hoàn -->
            <li class="nav-item">
                <a class="nav-link ${currentStatus == 'Đơn hàng hoàn' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/order-history?status=Đơn hàng hoàn">
                    <i class="bi bi-arrow-counterclockwise"></i> Đơn hàng hoàn 
                    <span class="badge bg-dark rounded-pill ms-1">${statusCounts['Đơn hàng hoàn']}</span>
                </a>
            </li>
        </ul>
    </div>

    <!-- Danh sách đơn hàng -->
    <c:choose>
        <c:when test="${not empty orders}">
            <c:forEach var="order" items="${orders}">
                <div class="order-card">
                    <!-- Order Card Header -->
                    <div class="order-header d-flex flex-wrap justify-content-between align-items-center">
                        <div class="mb-1 mb-md-0">
                            <span class="fw-bold text-dark me-2">Mã đơn: <span class="text-primary">#OD-${order.order_id}</span></span>
                            <span class="text-muted small">| Ngày đặt: <fmt:formatDate value="${order.created_date}" pattern="dd/MM/yyyy HH:mm" /></span>
                        </div>
                        <div>
                            <!-- Badge trạng thái động theo database -->
                            <span class="badge ${order.statusBadgeClass} status-badge">
                                <i class="bi ${order.statusIcon} me-1"></i> ${order.status}
                            </span>
                        </div>
                    </div>

                    <!-- Tiến trình xử lý (Stepper) trực quan -->
                    <c:choose>
                        <c:when test="${order.status == 'Đơn hàng hủy'}">
                            <div class="mx-3 mt-3 alert alert-danger py-2 mb-0 small">
                                <i class="bi bi-x-circle-fill me-1"></i> Đơn hàng này đã bị hủy. Toàn bộ sách đã được hoàn trả lại tồn kho.
                            </div>
                        </c:when>
                        <c:when test="${order.status == 'Đơn hàng hoàn'}">
                            <div class="mx-3 mt-3 alert alert-dark py-2 mb-0 small">
                                <i class="bi bi-arrow-counterclockwise me-1"></i> Đơn hàng hoàn trả về kho theo yêu cầu của khách hàng hoặc sự cố vận chuyển.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <!-- Quy trình tiến trình 6 bước thông thường -->
                            <div class="px-4 pt-3">
                                <div class="order-stepper">
                                    <div class="step-item ${order.status == 'Đơn hàng mới' ? 'active' : 'completed'}">
                                        <div class="step-dot"></div>
                                        <span>Đơn hàng mới</span>
                                    </div>
                                    <div class="step-item ${order.status == 'Đã xác nhận' ? 'active' : (order.status == 'Chuẩn bị hàng' or order.status == 'Vận chuyển' or order.status == 'Giao hàng' or order.status == 'Đã giao' ? 'completed' : '')}">
                                        <div class="step-dot"></div>
                                        <span>Đã xác nhận</span>
                                    </div>
                                    <div class="step-item ${order.status == 'Chuẩn bị hàng' ? 'active' : (order.status == 'Vận chuyển' or order.status == 'Giao hàng' or order.status == 'Đã giao' ? 'completed' : '')}">
                                        <div class="step-dot"></div>
                                        <span>Chuẩn bị hàng</span>
                                    </div>
                                    <div class="step-item ${order.status == 'Vận chuyển' ? 'active' : (order.status == 'Giao hàng' or order.status == 'Đã giao' ? 'completed' : '')}">
                                        <div class="step-dot"></div>
                                        <span>Vận chuyển</span>
                                    </div>
                                    <div class="step-item ${order.status == 'Giao hàng' ? 'active' : (order.status == 'Đã giao' ? 'completed' : '')}">
                                        <div class="step-dot"></div>
                                        <span>Giao hàng</span>
                                    </div>
                                    <div class="step-item ${order.status == 'Đã giao' ? 'active completed' : ''}">
                                        <div class="step-dot"></div>
                                        <span>Đã giao</span>
                                    </div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <!-- Danh sách các sản phẩm trong đơn hàng -->
                    <div class="p-3">
                        <c:forEach var="item" items="${order.items}">
                            <div class="d-flex align-items-center py-2 border-bottom">
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
                                <div class="flex-grow-1">
                                    <h6 class="mb-1">
                                        <a href="${pageContext.request.contextPath}/book-detail?id=${item.bookid}" class="text-dark fw-bold text-decoration-none">
                                            ${item.book.title}
                                        </a>
                                    </h6>
                                    <div class="text-muted small">
                                        Mã ISBN: ${item.book.isbn} | Nhà XB: ${item.book.publisher}
                                    </div>
                                    <div class="text-muted small mt-1">
                                        Số lượng: <strong class="text-dark">x${item.quantity}</strong> | Đơn giá: 
                                        <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                    </div>
                                </div>
                                <div class="text-end fw-bold text-danger">
                                    <fmt:formatNumber value="${item.subtotal}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Thông tin người nhận & Địa chỉ giao hàng -->
                    <div class="px-3 py-2 bg-light border-top border-bottom small">
                        <div class="row g-2">
                            <div class="col-md-6">
                                <div><strong>Người nhận:</strong> ${order.receiver_name} (${order.receiver_phone})</div>
                                <div><strong>Địa chỉ:</strong> ${order.receiver_address}</div>
                            </div>
                            <div class="col-md-6 text-md-end">
                                <div><strong>Thanh toán:</strong> <span class="badge bg-warning text-dark"><i class="bi bi-cash-stack"></i> ${order.payment_method} (Thanh toán khi nhận hàng)</span></div>
                                <c:if test="${not empty order.note}">
                                    <div><strong>Ghi chú:</strong> <em>"${order.note}"</em></div>
                                </c:if>
                            </div>
                        </div>
                    </div>

                    <!-- Card Footer: Tổng tiền & Nút thao tác -->
                    <div class="p-3 bg-white d-flex flex-wrap justify-content-between align-items-center">
                        <div>
                            <span class="text-muted">Tổng thanh toán (${order.totalQuantity} cuốn):</span>
                            <span class="fs-4 fw-bold text-danger ms-2">
                                <fmt:formatNumber value="${order.total_money}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                            </span>
                        </div>
                        <div class="mt-2 mt-md-0 d-flex gap-2">
                            <!-- Nút Xem chi tiết -->
                            <a href="${pageContext.request.contextPath}/order-detail?id=${order.order_id}" class="btn btn-outline-secondary btn-sm">
                                <i class="bi bi-eye"></i> Xem chi tiết
                            </a>

                            <!-- Nút Hủy đơn: Chỉ hiển thị và cho phép hủy khi ở trạng thái "Đơn hàng mới" -->
                            <c:if test="${order.canCancel}">
                                <form action="${pageContext.request.contextPath}/order-cancel" method="post" class="d-inline"
                                      onsubmit="return confirm('Bạn có chắc chắn muốn hủy đơn hàng #OD-${order.order_id}? Số lượng sách sẽ được hoàn lại vào kho.');">
                                    <input type="hidden" name="orderId" value="${order.order_id}">
                                    <button type="submit" class="btn btn-outline-danger btn-sm">
                                        <i class="bi bi-x-circle"></i> Hủy đơn hàng
                                    </button>
                                </form>
                            </c:if>
                        </div>
                    </div>

                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="card border-0 shadow-sm rounded-4 p-5 text-center my-4 bg-white">
                <div class="my-4">
                    <i class="bi bi-receipt-cutoff text-muted" style="font-size: 70px;"></i>
                    <h5 class="fw-bold mt-3 text-secondary">
                        Không có đơn hàng nào ở trạng thái "${currentStatus == 'all' ? 'Tất cả' : currentStatus}"!
                    </h5>
                    <p class="text-muted mb-4">
                        Bạn có thể chọn tab trạng thái khác hoặc mua thêm sách mới từ kho sách của chúng tôi.
                    </p>
                    <div class="d-flex justify-content-center gap-2">
                        <a href="${pageContext.request.contextPath}/order-history?status=all" class="btn btn-outline-primary">
                            Xem tất cả đơn hàng
                        </a>
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary fw-bold">
                            <i class="bi bi-book"></i> Mua sắm sách ngay
                        </a>
                    </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

</div>

</body>
</html>
