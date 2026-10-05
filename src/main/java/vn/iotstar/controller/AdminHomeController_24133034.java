package vn.iotstar.controller;

import vn.iotstar.service.IAuthorService_24133034;
import vn.iotstar.service.IBookService_24133034;
import vn.iotstar.service.IUserService_24133034;
import vn.iotstar.service.impl.AuthorServiceImpl_24133034;
import vn.iotstar.service.impl.BookServiceImpl_24133034;
import vn.iotstar.service.impl.UserServiceImpl_24133034;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/admin/home"})
public class AdminHomeController_24133034 extends HttpServlet {

    private final IBookService_24133034 bookService = new BookServiceImpl_24133034();
    private final IAuthorService_24133034 authorService = new AuthorServiceImpl_24133034();
    private final IUserService_24133034 userService = new UserServiceImpl_24133034();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int totalBooks = bookService.countAll();
        int totalAuthors = authorService.findAll().size();
        int totalUsers = userService.findAll().size();

        req.setAttribute("totalBooks", totalBooks);
        req.setAttribute("totalAuthors", totalAuthors);
        req.setAttribute("totalUsers", totalUsers);

        req.getRequestDispatcher("/views/admin/home.jsp").forward(req, resp);
    }
}
