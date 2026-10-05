package vn.iotstar.controller;

import vn.iotstar.model.Author_24133034;
import vn.iotstar.model.Book_24133034;
import vn.iotstar.service.IAuthorService_24133034;
import vn.iotstar.service.IBookService_24133034;
import vn.iotstar.service.impl.AuthorServiceImpl_24133034;
import vn.iotstar.service.impl.BookServiceImpl_24133034;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/home"})
public class HomeController_24133034 extends HttpServlet {

    private final IAuthorService_24133034 authorService = new AuthorServiceImpl_24133034();
    private final IBookService_24133034 bookService = new BookServiceImpl_24133034();

    private static final int PAGE_SIZE = 3; // 03 sản phẩm / trang

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Author_24133034> authors = authorService.findAll();

        int authorId = 1;
        String authorIdParam = req.getParameter("authorId");
        if (authorIdParam != null) {
            try {
                authorId = Integer.parseInt(authorIdParam);
            } catch (NumberFormatException ignored) {}
        } else if (authors != null && !authors.isEmpty()) {
            authorId = authors.get(0).getAuthor_id();
        }

        int currentPage = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException ignored) {}
        }

        Author_24133034 currentAuthor = authorService.findById(authorId);
        int totalBooks = bookService.countByAuthorId(authorId);
        int totalPages = (int) Math.ceil((double) totalBooks / PAGE_SIZE);
        if (totalPages == 0) totalPages = 1;

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        List<Book_24133034> books = bookService.findByAuthorId(authorId, currentPage, PAGE_SIZE);

        req.setAttribute("authors", authors);
        req.setAttribute("currentAuthor", currentAuthor);
        req.setAttribute("books", books);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalBooks", totalBooks);

        req.getRequestDispatcher("/views/user/home.jsp").forward(req, resp);
    }
}
