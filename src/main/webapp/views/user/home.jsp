<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang Chủ - BookStore</title>
    <style>
        .author-tab {
            margin-bottom: 25px;
        }
        .book-table-card {
            border: 1px solid #dee2e6;
            background: #fff;
            padding: 15px;
            height: 100%;
        }
        .book-table-card img {
            width: 100%;
            height: 220px;
            object-fit: cover;
            border-bottom: 1px solid #eee;
            margin-bottom: 10px;
        }
        .book-field {
            margin-bottom: 4px;
            font-size: 14px;
        }
        .book-title-link {
            font-weight: bold;
            color: #0d6efd;
            text-decoration: none;
        }
        .book-title-link:hover {
            text-decoration: underline;
        }
        .pagination-container {
            margin-top: 30px;
            text-align: center;
        }
        .pagination-container a, .pagination-container span {
            margin: 0 5px;
            font-size: 16px;
            text-decoration: none;
        }
    </style>
</head>
<body>

<div class="container my-4">

    <!-- Flash Messages giỏ hàng -->
    <c:if test="${not empty sessionScope.cart_success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.cart_success}
            <a href="${pageContext.request.contextPath}/cart" class="alert-link fw-bold ms-2">Xem giỏ hàng</a>
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
    <!-- Chọn tác giả -->
    <div class="row author-tab">
        <div class="col-12">
            <h5 class="fw-bold mb-3"><i class="bi bi-person-lines-fill"></i> Danh sách Tác Giả:</h5>
            <div class="btn-group flex-wrap" role="group">
                <c:forEach var="a" items="${authors}">
                    <a href="${pageContext.request.contextPath}/home?authorId=${a.author_id}&page=1"
                       class="btn ${a.author_id == currentAuthor.author_id ? 'btn-primary' : 'btn-outline-primary'} mb-1">
                        ${a.author_name}
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>

    <!-- Tiêu đề Tác giả theo đúng mẫu đề thi Câu 3 -->
    <div class="row mb-3">
        <div class="col-12 border-bottom pb-2">
            <h4 class="text-dark fw-bold">
                Tác giả :<span class="text-primary">${currentAuthor.author_name}</span>
            </h4>
        </div>
    </div>

    <!-- Hiển thị sách: 03 sản phẩm / trang -->
    <div class="row g-4">
        <c:choose>
            <c:when test="${not empty books}">
                <c:forEach var="book" items="${books}">
                    <div class="col-md-4">
                        <div class="book-table-card shadow-sm rounded">
                            <a href="${pageContext.request.contextPath}/book-detail?id=${book.bookid}">
                                <c:choose>
                                    <c:when test="${not empty book.cover_image}">
                                        <c:choose>
                                            <c:when test="${book.cover_image.startsWith('http://') or book.cover_image.startsWith('https://')}">
                                                <img src="${book.cover_image}" alt="${book.title}">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/uploads/${book.cover_image}"
                                                     onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/uploads/default-book.png'"
                                                     alt="${book.title}">
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/uploads/default-book.png" alt="${book.title}">
                                    </c:otherwise>
                                </c:choose>
                            </a>

                            <div class="book-field">
                                <strong>Tiêu đề:</strong>
                                <a href="${pageContext.request.contextPath}/book-detail?id=${book.bookid}" class="book-title-link">
                                    ${book.title}
                                </a>
                            </div>
                            <div class="book-field">
                                <strong>Mã isbn:</strong> ${book.isbn}
                            </div>
                            <div class="book-field">
                                <strong>Tác giả:</strong> ${empty book.author_name ? currentAuthor.author_name : book.author_name}
                            </div>
                            <div class="book-field">
                                <strong>Publisher:</strong> ${book.publisher}
                            </div>
                            <div class="book-field">
                                <strong>Publisher_date:</strong> ${book.publish_date}
                            </div>
                            <div class="book-field">
                                <strong>Quantity:</strong> ${book.quantity}
                            </div>
                            <div class="book-field text-danger fw-bold">
                                Review (${book.review_count})
                            </div>
                            <div class="mt-3 pt-2 border-top d-flex justify-content-between align-items-center">
                                <span class="fw-bold text-danger fs-6">
                                    <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </span>
                                <c:choose>
                                    <c:when test="${book.quantity > 0}">
                                        <form action="${pageContext.request.contextPath}/cart/add" method="post" class="d-inline mb-0">
                                            <input type="hidden" name="bookId" value="${book.bookid}">
                                            <input type="hidden" name="quantity" value="1">
                                            <button type="submit" class="btn btn-primary btn-sm px-2 py-1">
                                                <i class="bi bi-cart-plus"></i> Thêm giỏ
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">Hết hàng</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="col-12">
                    <div class="alert alert-info">Chưa có sách nào cho tác giả này!</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Phân trang theo đúng mẫu Câu 3: Trang trước – 1 2 3 4 – Trang sau -->
    <c:if test="${totalPages > 0}">
        <div class="pagination-container">
            <c:choose>
                <c:when test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/home?authorId=${currentAuthor.author_id}&page=${currentPage - 1}" class="text-primary fw-bold">
                        Trang trước
                    </a>
                </c:when>
                <c:otherwise>
                    <span class="text-muted">Trang trước</span>
                </c:otherwise>
            </c:choose>

            <span class="mx-2">–</span>

            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <strong class="btn btn-primary btn-sm px-3 py-1 mx-1 text-white">${i}</strong>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/home?authorId=${currentAuthor.author_id}&page=${i}" class="btn btn-outline-secondary btn-sm px-3 py-1 mx-1">
                            ${i}
                        </a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <span class="mx-2">–</span>

            <c:choose>
                <c:when test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/home?authorId=${currentAuthor.author_id}&page=${currentPage + 1}" class="text-primary fw-bold">
                        Trang sau
                    </a>
                </c:when>
                <c:otherwise>
                    <span class="text-muted">Trang sau</span>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>

</div>

</body>
</html>
