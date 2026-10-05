package vn.iotstar.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

@WebServlet(urlPatterns = {"/uploads/*"})
public class ImageServlet_24133034 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        int index = requestUri.indexOf("/uploads/");
        if (index == -1) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String fileName = requestUri.substring(index + 9);
        fileName = URLDecoder.decode(fileName, "UTF-8");

        File file = findImageFile(req, fileName);

        if (file == null || !file.exists() || !file.isFile()) {
            // Thử tìm default-book.png
            file = findImageFile(req, "default-book.png");
        }

        if (file != null && file.exists() && file.isFile()) {
            String mimeType = req.getServletContext().getMimeType(file.getName());
            if (mimeType == null) {
                if (file.getName().toLowerCase().endsWith(".png")) {
                    mimeType = "image/png";
                } else if (file.getName().toLowerCase().endsWith(".gif")) {
                    mimeType = "image/gif";
                } else {
                    mimeType = "image/jpeg";
                }
            }
            resp.setContentType(mimeType);
            resp.setContentLength((int) file.length());

            try (FileInputStream in = new FileInputStream(file);
                 OutputStream out = resp.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private File findImageFile(HttpServletRequest req, String fileName) {
        // 1. Thử lấy từ realPath của servlet context
        String realPath = req.getServletContext().getRealPath("/uploads");
        if (realPath != null) {
            File f = new File(realPath, fileName);
            if (f.exists() && f.isFile()) {
                return f;
            }
        }

        // 2. Thử lấy từ thư mục source src/main/webapp/uploads
        File fSrc = new File("src/main/webapp/uploads", fileName);
        if (fSrc.exists() && fSrc.isFile()) {
            return fSrc;
        }

        // 3. Thử lấy từ target/BookStore_24133034/uploads
        File fTarget = new File("target/BookStore_24133034/uploads", fileName);
        if (fTarget.exists() && fTarget.isFile()) {
            return fTarget;
        }

        // 4. Thử tìm với đường dẫn tuyệt đối trong workspace
        File fAbs = new File("c:/Users/One-Tura/Pictures/BookStore_24133034/src/main/webapp/uploads", fileName);
        if (fAbs.exists() && fAbs.isFile()) {
            return fAbs;
        }

        return null;
    }
}
