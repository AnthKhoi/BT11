package vn.iotstar.filter;

import vn.iotstar.model.User_24133034;
import vn.iotstar.util.Constant_24133034;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AdminSecurityFilter_24133034", urlPatterns = {"/admin/*"})
public class AdminSecurityFilter_24133034 implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        User_24133034 user = (session != null) ? (User_24133034) session.getAttribute(Constant_24133034.SESSION_USER) : null;

        if (user == null || !user.isIs_admin()) {
            resp.sendRedirect(req.getContextPath() + "/login?error=access_denied");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
