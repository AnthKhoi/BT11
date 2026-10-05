package vn.iotstar.controller;

import vn.iotstar.model.Book_24133034;
import vn.iotstar.model.Rating_24133034;
import vn.iotstar.model.User_24133034;
import vn.iotstar.service.IBookService_24133034;
import vn.iotstar.service.IRatingService_24133034;
import vn.iotstar.service.impl.BookServiceImpl_24133034;
import vn.iotstar.service.impl.RatingServiceImpl_24133034;
import vn.iotstar.util.Constant_24133034;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/book-detail", "/add-review"})
public class BookDetailController_24133034 extends HttpServlet {

    private final IBookService_24133034 bookService = new BookServiceImpl_24133034();
    private final IRatingService_24133034 ratingService = new RatingServiceImpl_24133034();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            int bookId = Integer.parseInt(idParam);
            Book_24133034 book = bookService.findById(bookId);
            if (book == null) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }

            List<Rating_24133034> reviews = ratingService.findByBookId(bookId);
            int reviewCount = ratingService.countByBookId(bookId);
            book.setReview_count(reviewCount);

            req.setAttribute("book", book);
            req.setAttribute("reviews", reviews);
            req.setAttribute("reviewCount", reviewCount);

            req.getRequestDispatcher("/views/user/book-detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User_24133034 user = (session != null) ? (User_24133034) session.getAttribute(Constant_24133034.SESSION_USER) : null;

        String bookIdParam = req.getParameter("bookid");
        if (bookIdParam == null) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        int bookId = Integer.parseInt(bookIdParam);

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=login_required");
            return;
        }

        String reviewText = req.getParameter("review_text");
        String ratingParam = req.getParameter("rating");
        int ratingValue = 5;
        if (ratingParam != null) {
            try {
                ratingValue = Integer.parseInt(ratingParam);
            } catch (NumberFormatException ignored) {}
        }

        Rating_24133034 rating = new Rating_24133034();
        rating.setUserid(user.getId());
        rating.setBookid(bookId);
        rating.setRating(ratingValue);
        rating.setReview_text(reviewText != null ? reviewText.trim() : "");

        ratingService.addOrUpdateRating(rating);

        resp.sendRedirect(req.getContextPath() + "/book-detail?id=" + bookId);
    }
}
