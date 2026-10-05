<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>${isEdit ? 'Chỉnh Sửa Sách' : 'Thêm Sách Mới'} - BookStore</title>
</head>
<body>
<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="fw-bold">
            <i class="bi ${isEdit ? 'bi-pencil-square' : 'bi-plus-circle'}"></i>
            ${isEdit ? 'Chỉnh Sửa Thông Tin Sách' : 'Thêm Sách Mới'}
        </h3>
        <a href="${pageContext.request.contextPath}/admin/books" class="btn btn-outline-secondary">
            <i class="bi bi-arrow-left"></i> Quay lại danh sách
        </a>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger" role="alert">
            <i class="bi bi-exclamation-triangle-fill"></i> ${errorMessage}
        </div>
    </c:if>

    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-4">
            <form action="${pageContext.request.contextPath}${isEdit ? '/admin/book/edit' : '/admin/book/create'}"
                  method="post" enctype="multipart/form-data" accept-charset="UTF-8">

                <c:if test="${isEdit}">
                    <input type="hidden" name="bookid" value="${book.bookid}">
                    <input type="hidden" name="old_cover_image" value="${book.cover_image}">
                </c:if>

                <div class="row g-3">
                    <div class="col-md-8">
                        <label class="form-label fw-bold">Tiêu đề sách (*):</label>
                        <input type="text" name="title" class="form-control" value="${book.title}" placeholder="Nhập tên sách" required>
                    </div>

                    <div class="col-md-4">
                        <label class="form-label fw-bold">Mã ISBN (*):</label>
                        <input type="number" name="isbn" class="form-control" value="${book.isbn}" placeholder="Ví dụ: 1001" required>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-bold">Nhà xuất bản (Publisher):</label>
                        <input type="text" name="publisher" class="form-control" value="${book.publisher}" placeholder="Nhập nhà xuất bản">
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-bold">Đơn giá (VNĐ):</label>
                        <input type="number" step="any" name="price" class="form-control" value="${book.price}" placeholder="100000" required>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label fw-bold">Số lượng (Quantity):</label>
                        <input type="number" name="quantity" class="form-control" value="${book.quantity}" placeholder="0" required>
                    </div>

                    <div class="col-md-4">
                        <label class="form-label fw-bold">Ngày xuất bản (Publish Date):</label>
                        <input type="date" name="publish_date" class="form-control" value="${book.publish_date}">
                    </div>

                    <div class="col-md-12">
                        <label class="form-label fw-bold">Tác giả (Tích chọn một hoặc nhiều tác giả):</label>
                        <div class="p-3 border rounded bg-white shadow-sm mb-2" style="max-height: 180px; overflow-y: auto;">
                            <div class="row g-2">
                                <c:forEach var="a" items="${authors}">
                                    <c:set var="isSelected" value="false" />
                                    <c:if test="${isEdit and not empty book.authors}">
                                        <c:forEach var="ba" items="${book.authors}">
                                            <c:if test="${ba.author_id == a.author_id}">
                                                <c:set var="isSelected" value="true" />
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                    <div class="col-md-4 col-sm-6">
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" name="authorIds" value="${a.author_id}" id="auth_${a.author_id}" ${isSelected ? 'checked' : ''}>
                                            <label class="form-check-label fw-semibold" for="auth_${a.author_id}">
                                                ${a.author_name}
                                            </label>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <label class="form-label fw-bold mt-2">Hoặc thêm tác giả mới (nếu chưa có trong danh sách trên):</label>
                        <input type="text" name="new_authors" class="form-control" placeholder="Nhập tên một hoặc nhiều tác giả mới (ví dụ: Dale Carnegie, Nguyễn Hiến Lê)">
                        <small class="text-muted">Có thể nhập nhiều tác giả mới cách nhau bằng dấu phẩy (,)</small>
                    </div>

                    <div class="col-md-12">
                        <label class="form-label fw-bold">Ảnh bìa (Cover Image):</label>
                        <div class="row g-2">
                            <div class="col-md-6">
                                <label class="form-label small text-muted">Cách 1: Tải file ảnh từ máy tính</label>
                                <input type="file" name="cover_image_file" class="form-control" accept="image/*">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small text-muted">Cách 2: Hoặc dán đường link ảnh trực tuyến (URL)</label>
                                <input type="text" name="cover_image_url" class="form-control" value="${book.cover_image.startsWith('http') ? book.cover_image : ''}" placeholder="https://example.com/anh-bia-sach.jpg">
                            </div>
                        </div>
                        <c:if test="${isEdit and not empty book.cover_image}">
                            <div class="mt-2">
                                <small class="text-muted">Ảnh hiện tại:</small><br>
                                <c:choose>
                                    <c:when test="${book.cover_image.startsWith('http://') or book.cover_image.startsWith('https://')}">
                                        <img src="${book.cover_image}" class="img-thumbnail" style="max-height: 100px;">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/uploads/${book.cover_image}"
                                             onerror="this.src='${pageContext.request.contextPath}/uploads/default-book.png'"
                                             class="img-thumbnail" style="max-height: 100px;">
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                    </div>

                    <div class="col-md-12">
                        <label class="form-label fw-bold">Mô tả tóm tắt (Description):</label>
                        <textarea name="description" rows="4" class="form-control" placeholder="Mô tả nội dung cuốn sách...">${book.description}</textarea>
                    </div>

                    <div class="col-12 mt-4 text-end">
                        <a href="${pageContext.request.contextPath}/admin/books" class="btn btn-secondary me-2">Hủy Bỏ</a>
                        <button type="submit" class="btn btn-primary px-4 fw-bold">
                            <i class="bi bi-save"></i> ${isEdit ? 'Lưu Cập Nhật' : 'Thêm Cuốn Sách'}
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
