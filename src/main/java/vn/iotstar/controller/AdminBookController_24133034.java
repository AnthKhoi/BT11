package vn.iotstar.controller;

import vn.iotstar.model.Author_24133034;
import vn.iotstar.model.Book_24133034;
import vn.iotstar.service.IAuthorService_24133034;
import vn.iotstar.service.IBookService_24133034;
import vn.iotstar.service.impl.AuthorServiceImpl_24133034;
import vn.iotstar.service.impl.BookServiceImpl_24133034;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/books", "/admin/book/create", "/admin/book/edit", "/admin/book/delete"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
        maxFileSize = 1024 * 1024 * 10,       // 10MB
        maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class AdminBookController_24133034 extends HttpServlet {

    private final IBookService_24133034 bookService = new BookServiceImpl_24133034();
    private final IAuthorService_24133034 authorService = new AuthorServiceImpl_24133034();

    private static final int ADMIN_PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();

        switch (path) {
            case "/admin/book/create":
                showCreateForm(req, resp);
                break;
            case "/admin/book/edit":
                showEditForm(req, resp);
                break;
            case "/admin/book/delete":
                deleteBook(req, resp);
                break;
            case "/admin/books":
            default:
                listBooks(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();

        if ("/admin/book/create".equals(path)) {
            insertBook(req, resp);
        } else if ("/admin/book/edit".equals(path)) {
            updateBook(req, resp);
        } else {
            listBooks(req, resp);
        }
    }

    private void listBooks(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) page = 1;
            } catch (NumberFormatException ignored) {}
        }

        int totalBooks = bookService.countAll();
        int totalPages = (int) Math.ceil((double) totalBooks / ADMIN_PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;
        if (page > totalPages) page = totalPages;

        List<Book_24133034> books = bookService.findAll(page, ADMIN_PAGE_SIZE);

        req.setAttribute("books", books);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalBooks", totalBooks);

        req.getRequestDispatcher("/views/admin/book-list.jsp").forward(req, resp);
    }

    private void showCreateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Author_24133034> authors = authorService.findAll();
        req.setAttribute("authors", authors);
        req.setAttribute("isEdit", false);
        req.getRequestDispatcher("/views/admin/book-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null) {
            try {
                int bookId = Integer.parseInt(idParam);
                Book_24133034 book = bookService.findById(bookId);
                if (book != null) {
                    List<Author_24133034> authors = authorService.findAll();
                    req.setAttribute("authors", authors);
                    req.setAttribute("book", book);
                    req.setAttribute("isEdit", true);
                    req.getRequestDispatcher("/views/admin/book-form.jsp").forward(req, resp);
                    return;
                }
            } catch (NumberFormatException ignored) {}
        }
        resp.sendRedirect(req.getContextPath() + "/admin/books");
    }

    private void insertBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book_24133034 book = extractBookFromRequest(req);
        String[] authorIdParams = req.getParameterValues("authorIds");
        List<Integer> authorIds = parseAuthorIds(authorIdParams);

        // Xử lý thêm nhanh tác giả mới (nếu có nhập)
        processNewAuthors(req, authorIds);

        // Upload ảnh bìa (file upload hoặc link URL trực tuyến)
        String fileName = handleFileUpload(req);
        String coverUrl = getParamUtf8(req, "cover_image_url");

        if (fileName != null && !fileName.isEmpty()) {
            book.setCover_image(fileName);
        } else if (coverUrl != null && !coverUrl.trim().isEmpty()) {
            book.setCover_image(coverUrl.trim());
        } else {
            book.setCover_image("default-book.png");
        }

        boolean success = bookService.insert(book, authorIds);
        if (success) {
            resp.sendRedirect(req.getContextPath() + "/admin/books?message=create_success");
        } else {
            req.setAttribute("errorMessage", "Thêm sách thất bại! Vui lòng thử lại.");
            showCreateForm(req, resp);
        }
    }

    private void updateBook(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book_24133034 book = extractBookFromRequest(req);
        String bookIdParam = req.getParameter("bookid");
        if (bookIdParam != null) {
            book.setBookid(Integer.parseInt(bookIdParam));
        }

        String[] authorIdParams = req.getParameterValues("authorIds");
        List<Integer> authorIds = parseAuthorIds(authorIdParams);

        // Xử lý thêm nhanh tác giả mới (nếu có nhập)
        processNewAuthors(req, authorIds);

        // Upload ảnh mới nếu người dùng chọn hoặc dán URL mới
        String fileName = handleFileUpload(req);
        String coverUrl = getParamUtf8(req, "cover_image_url");

        if (fileName != null && !fileName.isEmpty()) {
            book.setCover_image(fileName);
        } else if (coverUrl != null && !coverUrl.trim().isEmpty()) {
            book.setCover_image(coverUrl.trim());
        } else {
            String oldCover = req.getParameter("old_cover_image");
            book.setCover_image(oldCover != null ? oldCover : "default-book.png");
        }

        boolean success = bookService.update(book, authorIds);
        if (success) {
            resp.sendRedirect(req.getContextPath() + "/admin/books?message=update_success");
        } else {
            req.setAttribute("errorMessage", "Cập nhật sách thất bại! Vui lòng thử lại.");
            showEditForm(req, resp);
        }
    }

    private void deleteBook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam != null) {
            try {
                int bookId = Integer.parseInt(idParam);
                bookService.delete(bookId);
            } catch (NumberFormatException ignored) {}
        }
        resp.sendRedirect(req.getContextPath() + "/admin/books?message=delete_success");
    }

    private void processNewAuthors(HttpServletRequest req, List<Integer> authorIds) {
        String newAuthorsParam = getParamUtf8(req, "new_authors");
        if (newAuthorsParam != null && !newAuthorsParam.trim().isEmpty()) {
            String[] names = newAuthorsParam.split(",");
            for (String name : names) {
                name = name.trim();
                if (!name.isEmpty()) {
                    Author_24133034 existing = null;
                    for (Author_24133034 a : authorService.findAll()) {
                        if (a.getAuthor_name().equalsIgnoreCase(name)) {
                            existing = a;
                            break;
                        }
                    }
                    if (existing != null) {
                        if (!authorIds.contains(existing.getAuthor_id())) {
                            authorIds.add(existing.getAuthor_id());
                        }
                    } else {
                        Author_24133034 newA = new Author_24133034();
                        newA.setAuthor_name(name);
                        authorService.insert(newA);

                        for (Author_24133034 a : authorService.findAll()) {
                            if (a.getAuthor_name().equalsIgnoreCase(name)) {
                                if (!authorIds.contains(a.getAuthor_id())) {
                                    authorIds.add(a.getAuthor_id());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private Book_24133034 extractBookFromRequest(HttpServletRequest req) {
        Book_24133034 book = new Book_24133034();

        String isbnParam = req.getParameter("isbn");
        if (isbnParam != null && !isbnParam.isEmpty()) {
            try { book.setIsbn(Integer.parseInt(isbnParam)); } catch (NumberFormatException ignored) {}
        }

        book.setTitle(getParamUtf8(req, "title"));
        book.setPublisher(getParamUtf8(req, "publisher"));

        String priceParam = req.getParameter("price");
        if (priceParam != null && !priceParam.isEmpty()) {
            try { book.setPrice(new BigDecimal(priceParam.trim())); } catch (Exception ignored) {}
        }

        book.setDescription(getParamUtf8(req, "description"));

        String dateParam = req.getParameter("publish_date");
        if (dateParam != null && !dateParam.isEmpty()) {
            try { book.setPublish_date(Date.valueOf(dateParam.trim())); } catch (Exception ignored) {}
        }

        String quantityParam = req.getParameter("quantity");
        if (quantityParam != null && !quantityParam.isEmpty()) {
            try { book.setQuantity(Integer.parseInt(quantityParam.trim())); } catch (NumberFormatException ignored) {}
        }

        return book;
    }

    private String getParamUtf8(HttpServletRequest req, String paramName) {
        String val = req.getParameter(paramName);
        if (val == null) return null;
        val = val.trim();
        try {
            // Khắc phục lỗi Tomcat decode nhầm multipart bằng ISO-8859-1
            byte[] bytes = val.getBytes("ISO-8859-1");
            String utf8 = new String(bytes, "UTF-8");
            if (utf8.length() != val.length()) {
                return utf8;
            }
        } catch (Exception ignored) {}
        return val;
    }

    private List<Integer> parseAuthorIds(String[] authorIdParams) {
        List<Integer> list = new ArrayList<>();
        if (authorIdParams != null) {
            for (String s : authorIdParams) {
                try {
                    list.add(Integer.parseInt(s.trim()));
                } catch (NumberFormatException ignored) {}
            }
        }
        return list;
    }

    private String handleFileUpload(HttpServletRequest req) {
        try {
            Part part = req.getPart("cover_image_file");
            if (part != null && part.getSize() > 0) {
                String submittedFileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
                String ext = "";
                int dotIndex = submittedFileName.lastIndexOf('.');
                if (dotIndex >= 0) {
                    ext = submittedFileName.substring(dotIndex);
                }
                String newFileName = "book_" + System.currentTimeMillis() + ext;

                // Lưu vào thư mục webapps runtime
                String uploadPath = req.getServletContext().getRealPath("/uploads");
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                File targetFile = new File(uploadDir, newFileName);
                part.write(targetFile.getAbsolutePath());

                // Đồng thời lưu vào thư mục source code nếu có để không bị mất khi build lại
                try {
                    File sourceUploadDir = new File("src/main/webapp/uploads");
                    if (sourceUploadDir.exists()) {
                        Files.copy(targetFile.toPath(), new File(sourceUploadDir, newFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (Exception ignored) {}

                return newFileName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
