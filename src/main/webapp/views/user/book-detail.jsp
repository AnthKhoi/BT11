<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${book.title} - Chi tiết sách</title>
    <style>
        .detail-box {
            border: 1px solid #000;
            background-color: #fff;
            padding: 0;
            margin-bottom: 25px;
        }
        .detail-table {
            width: 100%;
            margin-bottom: 0;
            border-collapse: collapse;
        }
        .detail-table td {
            border: 1px solid #000;
            padding: 15px;
            vertical-align: top;
        }
        .cover-col {
            width: 35%;
            text-align: center;
        }
        .cover-img {
            max-width: 100%;
            max-height: 380px;
            object-fit: contain;
        }
        .review-section {
            border: 1px solid #000;
            border-top: none;
            padding: 15px;
            background-color: #fff;
        }
        .review-header {
            font-weight: bold;
            font-size: 18px;
            border-bottom: 1px solid #000;
            padding-bottom: 5px;
            margin-bottom: 15px;
        }
        .review-item {
            margin-bottom: 10px;
            padding: 8px 12px;
            background-color: #f8f9fa;
            border-radius: 4px;
            border-left: 3px solid #0d6efd;
        }
        .form-review-box {
            border: 1px solid #000;
            border-top: none;
            padding: 15px;
            background-color: #fff;
        }
    </style>
</head>
<body>

<div class="container my-4">
    <c:if test="${not empty sessionScope.cart_success}">
        <div class="alert alert-success alert-dismissible fade show mb-3" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.cart_success}
            <a href="${pageContext.request.contextPath}/cart" class="alert-link fw-bold ms-2">Xem giỏ hàng</a>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="cart_success" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.cart_warning}">
        <div class="alert alert-warning alert-dismissible fade show mb-3" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${sessionScope.cart_warning}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="cart_warning" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.cart_error}">
        <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
            <i class="bi bi-x-circle-fill me-2"></i> ${sessionScope.cart_error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="cart_error" scope="session" />
    </c:if>

    <div class="mb-3 d-flex justify-content-between align-items-center">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-arrow-left"></i> Quay lại trang chủ
        </a>
        <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-primary btn-sm">
            <i class="bi bi-cart3"></i> Xem giỏ hàng
        </a>
    </div>

    <!-- Bảng thông tin chi tiết sách theo mẫu Câu 4 -->
    <div class="detail-box">
        <table class="detail-table">
            <tr>
                <td class="cover-col">
                    <c:choose>
                        <c:when test="${not empty book.cover_image}">
                            <c:choose>
                                <c:when test="${book.cover_image.startsWith('http://') or book.cover_image.startsWith('https://')}">
                                    <img src="${book.cover_image}" alt="${book.title}" class="cover-img">
                                </c:when>
                                <c:otherwise>
                                    <img src="${pageContext.request.contextPath}/uploads/${book.cover_image}"
                                         onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/uploads/default-book.png'"
                                         alt="${book.title}" class="cover-img">
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <img src="${pageContext.request.contextPath}/uploads/default-book.png" alt="${book.title}" class="cover-img">
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <h3 class="fw-bold mb-3">${book.title}</h3>
                    <p class="mb-2"><strong>Tiêu đề:</strong> ${book.title}</p>
                    <p class="mb-2"><strong>Mã isbn:</strong> ${book.isbn}</p>
                    <p class="mb-2"><strong>Tác giả:</strong>
                        <c:choose>
                            <c:when test="${not empty book.authors}">
                                <c:forEach var="a" items="${book.authors}" varStatus="status">
                                    ${a.author_name}${!status.last ? ', ' : ''}
                                </c:forEach>
                            </c:when>
                            <c:otherwise>${book.author_name}</c:otherwise>
                        </c:choose>
                    </p>
                    <p class="mb-2"><strong>Publisher:</strong> ${book.publisher}</p>
                    <p class="mb-2"><strong>Publisher_date:</strong> ${book.publish_date}</p>
                    <p class="mb-2"><strong>Đơn giá:</strong> 
                        <span class="fs-4 fw-bold text-danger">
                            <fmt:formatNumber value="${book.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                        </span>
                    </p>
                    <p class="mb-2"><strong>Quantity (Tồn kho):</strong> 
                        <span class="${book.quantity > 0 ? 'text-success fw-bold' : 'text-danger fw-bold'}">
                            ${book.quantity > 0 ? book.quantity += ' cuốn còn lại' : 'Tạm hết hàng'}
                        </span>
                    </p>
                    <p class="mb-2 text-danger fw-bold fs-5">
                        Reviews (${reviewCount})
                    </p>
                    
                    <!-- Phần thao tác Giỏ hàng (Thêm giỏ & Mua ngay) -->
                    <div class="my-4 p-3 bg-light rounded border">
                        <c:choose>
                            <c:when test="${book.quantity > 0}">
                                <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                    <input type="hidden" name="bookId" value="${book.bookid}">
                                    
                                    <div class="row align-items-center g-3">
                                        <div class="col-auto">
                                            <label class="form-label fw-bold mb-0">Số lượng mua:</label>
                                        </div>
                                        <div class="col-auto">
                                            <div class="input-group" style="width: 140px;">
                                                <button type="button" class="btn btn-outline-secondary" onclick="decQty()">-</button>
                                                <input type="number" id="detailQty" name="quantity" value="1" min="1" max="${book.quantity}" class="form-control text-center fw-bold" required>
                                                <button type="button" class="btn btn-outline-secondary" onclick="incQty(${book.quantity})">+</button>
                                            </div>
                                        </div>
                                        <div class="col-auto">
                                            <span class="text-muted small">(Tối đa ${book.quantity} cuốn)</span>
                                        </div>
                                    </div>

                                    <div class="mt-3 d-flex gap-2">
                                        <button type="submit" name="action" value="add" class="btn btn-outline-primary px-4 py-2 fw-semibold">
                                            <i class="bi bi-cart-plus-fill me-1"></i> Thêm vào giỏ hàng
                                        </button>
                                        <button type="submit" name="action" value="buy_now" class="btn btn-danger px-4 py-2 fw-bold shadow-sm">
                                            <i class="bi bi-lightning-fill me-1"></i> Mua ngay (COD)
                                        </button>
                                    </div>
                                </form>
                                <script>
                                    function decQty() {
                                        var input = document.getElementById('detailQty');
                                        var val = parseInt(input.value) || 1;
                                        if (val > 1) input.value = val - 1;
                                    }
                                    function incQty(max) {
                                        var input = document.getElementById('detailQty');
                                        var val = parseInt(input.value) || 1;
                                        if (val < max) input.value = val + 1;
                                    }
                                </script>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-secondary mb-0">
                                    <i class="bi bi-info-circle"></i> Sản phẩm hiện đang tạm hết hàng, quý khách vui lòng quay lại sau!
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <c:if test="${not empty book.description}">
                        <div class="mt-3 text-muted">
                            <strong>Mô tả:</strong> ${book.description}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>

        <!-- Mục Reviews theo mẫu Câu 4 -->
        <div class="review-section">
            <div class="review-header">Reviews</div>
            <c:choose>
                <c:when test="${not empty reviews}">
                    <c:forEach var="rev" items="${reviews}">
                        <div class="review-item">
                            <strong>[${empty rev.user_fullname ? rev.user_email : rev.user_fullname}]:</strong>
                            <span class="text-warning ms-2">
                                <c:forEach begin="1" end="${rev.rating}">★</c:forEach>
                            </span>
                            <span class="ms-2">${rev.review_text}</span>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="text-muted fst-italic">Chưa có đánh giá nào cho cuốn sách này. Hãy là người đầu tiên đánh giá!</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Form thêm reviews theo mẫu Câu 4 -->
        <div class="form-review-box">
            <div class="review-header">Form thêm reviews</div>
            <c:choose>
                <c:when test="${not empty sessionScope.account}">
                    <form action="${pageContext.request.contextPath}/add-review" method="post">
                        <input type="hidden" name="bookid" value="${book.bookid}">

                        <div class="mb-3">
                            <label class="form-label fw-bold">Đánh giá số sao:</label>
                            <select name="rating" class="form-select w-auto">
                                <option value="5">5 sao - Rất tuyệt vời</option>
                                <option value="4">4 sao - Hài lòng</option>
                                <option value="3">3 sao - Bình thường</option>
                                <option value="2">2 sao - Kém</option>
                                <option value="1">1 sao - Rất tệ</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Nội dung đánh giá:</label>
                            <textarea name="review_text" rows="3" class="form-control" placeholder="Nhập cảm nhận của bạn về cuốn sách này..." required></textarea>
                        </div>

                        <div>
                            <button type="submit" class="btn btn-primary px-4 fw-bold">[Submit]</button>
                        </div>
                    </form>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-warning mb-0">
                        Vui lòng <a href="${pageContext.request.contextPath}/login" class="fw-bold alert-link">Đăng nhập</a> để gửi đánh giá cho cuốn sách này!
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

</body>
</html>
