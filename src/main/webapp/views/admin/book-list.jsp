<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản Lý Sách - BookStore</title>
</head>
<body>
<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fw-bold"><i class="bi bi-book"></i> Danh Sách Sách (CRUD & Phân Trang)</h3>
        <a href="${pageContext.request.contextPath}/admin/book/create" class="btn btn-success">
            <i class="bi bi-plus-circle"></i> Thêm Sách Mới
        </a>
    </div>

    <!-- Thông báo kết quả thao tác -->
    <c:if test="${param.message == 'create_success'}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle"></i> Thêm sách mới thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.message == 'update_success'}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle"></i> Cập nhật thông tin sách thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.message == 'delete_success'}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle"></i> Xóa sách thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Bảng danh sách sách -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover table-striped align-middle mb-0">
                    <thead class="table-dark">
                        <tr>
                            <th scope="col" class="text-center" style="width: 60px;">ID</th>
                            <th scope="col" style="width: 90px;">Ảnh Bìa</th>
                            <th scope="col">Tiêu Đề</th>
                            <th scope="col">Mã ISBN</th>
                            <th scope="col">Tác Giả</th>
                            <th scope="col">Nhà XB</th>
                            <th scope="col">Đơn Giá</th>
                            <th scope="col" class="text-center">Số Lượng</th>
                            <th scope="col" class="text-center" style="width: 140px;">Thao Tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty books}">
                                <c:forEach var="b" items="${books}">
                                    <tr>
                                        <td class="text-center fw-bold">${b.bookid}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty b.cover_image}">
                                                    <c:choose>
                                                        <c:when test="${b.cover_image.startsWith('http://') or b.cover_image.startsWith('https://')}">
                                                            <img src="${b.cover_image}"
                                                                 class="img-thumbnail" style="width: 60px; height: 75px; object-fit: cover;">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <img src="${pageContext.request.contextPath}/uploads/${b.cover_image}"
                                                                 onerror="this.src='${pageContext.request.contextPath}/uploads/default-book.png'"
                                                                 class="img-thumbnail" style="width: 60px; height: 75px; object-fit: cover;">
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${pageContext.request.contextPath}/uploads/default-book.png" class="img-thumbnail" style="width: 60px; height: 75px; object-fit: cover;">
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/book-detail?id=${b.bookid}" target="_blank" class="fw-bold text-decoration-none text-primary">
                                                ${b.title}
                                            </a>
                                            <br>
                                            <small class="text-muted">Ngày XB: ${b.publish_date}</small>
                                        </td>
                                        <td>${b.isbn}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty b.authors}">
                                                    <c:forEach var="a" items="${b.authors}" varStatus="st">
                                                        <span class="badge bg-secondary">${a.author_name}</span>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${b.author_name}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${b.publisher}</td>
                                        <td class="fw-bold text-danger"><fmt:formatNumber value="${b.price}" pattern="#,###" /> VNĐ</td>
                                        <td class="text-center">${b.quantity}</td>
                                        <td class="text-center">
                                            <a href="${pageContext.request.contextPath}/admin/book/edit?id=${b.bookid}"
                                               class="btn btn-warning btn-sm me-1" title="Chỉnh sửa">
                                                <i class="bi bi-pencil-square"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/book/delete?id=${b.bookid}"
                                               class="btn btn-danger btn-sm"
                                               onclick="return confirm('Bạn có chắc chắn muốn xóa cuốn sách này không?');"
                                               title="Xóa">
                                                <i class="bi bi-trash"></i>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="9" class="text-center py-4 text-muted">
                                        Chưa có sách nào trong cơ sở dữ liệu!
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Phân trang quản trị viên (Câu 6) -->
        <c:if test="${totalPages > 1}">
            <div class="card-footer bg-white d-flex justify-content-between align-items-center py-3">
                <span class="text-muted small">
                    Hiển thị trang <strong>${currentPage}</strong> / <strong>${totalPages}</strong> (Tổng cộng: ${totalBooks} cuốn sách)
                </span>
                <nav>
                    <ul class="pagination pagination-sm mb-0">
                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/admin/books?page=${currentPage - 1}">Trước</a>
                        </li>
                        <c:forEach var="p" begin="1" end="${totalPages}">
                            <li class="page-item ${p == currentPage ? 'active' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/admin/books?page=${p}">${p}</a>
                            </li>
                        </c:forEach>
                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                            <a class="page-link" href="${pageContext.request.contextPath}/admin/books?page=${currentPage + 1}">Sau</a>
                        </li>
                    </ul>
                </nav>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>
