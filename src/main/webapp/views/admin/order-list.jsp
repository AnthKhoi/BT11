<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Đơn hàng - Admin</title>
</head>
<body>

<div class="d-flex justify-content-between align-items-center mb-3">
    <div>
        <h3 class="fw-bold text-dark mb-1"><i class="bi bi-cart-check"></i> Quản Lý Đơn Hàng</h3>
        <p class="text-muted small mb-0">Xem và cập nhật trạng thái đơn hàng</p>
    </div>
</div>

<c:if test="${not empty sessionScope.admin_order_success}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.admin_order_success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="admin_order_success" scope="session" />
</c:if>

<!-- Filter Tabs -->
<div class="card mb-4 border-0 shadow-sm">
    <div class="card-body p-2">
        <ul class="nav nav-pills flex-wrap">
            <li class="nav-item">
                <a class="nav-link ${currentStatus == 'all' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/admin/orders?status=all">
                    Tất cả <span class="badge bg-secondary ms-1">${statusCounts['all']}</span>
                </a>
            </li>
            <c:forEach var="st" items="${allStatuses}">
                <li class="nav-item">
                    <a class="nav-link ${currentStatus == st ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/admin/orders?status=${st}">
                        ${st} <span class="badge bg-light text-dark border ms-1">${statusCounts[st]}</span>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

<!-- Table Orders -->
<div class="card border-0 shadow-sm rounded-3">
    <div class="card-body p-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Mã đơn</th>
                        <th>Khách hàng</th>
                        <th>Thông tin giao hàng</th>
                        <th>Tổng tiền (COD)</th>
                        <th>Ngày đặt</th>
                        <th>Trạng thái hiện tại</th>
                        <th class="text-center" style="min-width: 220px;">Cập nhật trạng thái</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty orders}">
                            <c:forEach var="o" items="${orders}">
                                <tr>
                                    <td><strong class="text-primary">#OD-${o.order_id}</strong></td>
                                    <td>
                                        <div class="fw-semibold">${o.receiver_name}</div>
                                        <div class="small text-muted">${o.user_email}</div>
                                    </td>
                                    <td>
                                        <div class="small"><strong>SĐT:</strong> ${o.receiver_phone}</div>
                                        <div class="small text-muted text-truncate" style="max-width: 250px;" title="${o.receiver_address}">
                                            ${o.receiver_address}
                                        </div>
                                    </td>
                                    <td>
                                        <span class="fw-bold text-danger">
                                            <fmt:formatNumber value="${o.total_money}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                        </span>
                                    </td>
                                    <td class="small text-muted">
                                        <fmt:formatDate value="${o.created_date}" pattern="dd/MM/yyyy HH:mm" />
                                    </td>
                                    <td>
                                        <span class="badge ${o.statusBadgeClass} px-2 py-1">
                                            <i class="bi ${o.statusIcon} me-1"></i> ${o.status}
                                        </span>
                                    </td>
                                    <td class="text-center">
                                        <!-- Form cập nhật trạng thái ngay trên giao diện -->
                                        <form action="${pageContext.request.contextPath}/admin/orders/update-status" method="post" class="d-flex align-items-center justify-content-center gap-1">
                                            <input type="hidden" name="orderId" value="${o.order_id}">
                                            <select name="newStatus" class="form-select form-select-sm" style="font-size: 12px; width: 150px;">
                                                <c:forEach var="st" items="${allStatuses}">
                                                    <option value="${st}" ${o.status == st ? 'selected' : ''}>${st}</option>
                                                </c:forEach>
                                            </select>
                                            <button type="submit" class="btn btn-sm btn-primary" title="Lưu trạng thái">
                                                <i class="bi bi-check-lg"></i>
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" class="text-center py-4 text-muted">
                                    <i class="bi bi-inbox fs-3 d-block mb-1"></i>
                                    Không có đơn hàng nào ở trạng thái này!
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>
