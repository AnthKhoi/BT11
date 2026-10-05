package vn.iotstar.dao.impl;

import vn.iotstar.connection.DBConnection_24133034;
import vn.iotstar.dao.IBookDao_24133034;
import vn.iotstar.model.Book_24133034;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl_24133034 implements IBookDao_24133034 {

    @Override
    public List<Book_24133034> findAll(int page, int pageSize) {
        List<Book_24133034> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT b.*, "
                   + "  (SELECT STRING_AGG(a.author_name, ', ') FROM dbo.author a "
                   + "   INNER JOIN dbo.book_author ba ON a.author_id = ba.author_id WHERE ba.bookid = b.bookid) AS author_name, "
                   + "  (SELECT COUNT(*) FROM dbo.rating r WHERE r.bookid = b.bookid) AS review_count "
                   + "FROM dbo.books b "
                   + "ORDER BY b.bookid ASC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM dbo.books";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Book_24133034 findById(int bookid) {
        String sql = "SELECT b.*, "
                   + "  (SELECT STRING_AGG(a.author_name, ', ') FROM dbo.author a "
                   + "   INNER JOIN dbo.book_author ba ON a.author_id = ba.author_id WHERE ba.bookid = b.bookid) AS author_name, "
                   + "  (SELECT COUNT(*) FROM dbo.rating r WHERE r.bookid = b.bookid) AS review_count "
                   + "FROM dbo.books b WHERE b.bookid = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBook(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(Book_24133034 book) {
        String sql = "INSERT INTO dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getPublisher());
            ps.setBigDecimal(4, book.getPrice());
            ps.setString(5, book.getDescription());
            ps.setDate(6, book.getPublish_date());
            ps.setString(7, book.getCover_image());
            ps.setInt(8, book.getQuantity());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean update(Book_24133034 book) {
        String sql = "UPDATE dbo.books SET isbn = ?, title = ?, publisher = ?, price = ?, description = ?, "
                   + "publish_date = ?, cover_image = ?, quantity = ? WHERE bookid = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getPublisher());
            ps.setBigDecimal(4, book.getPrice());
            ps.setString(5, book.getDescription());
            ps.setDate(6, book.getPublish_date());
            ps.setString(7, book.getCover_image());
            ps.setInt(8, book.getQuantity());
            ps.setInt(9, book.getBookid());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int bookid) {
        String sql = "DELETE FROM dbo.books WHERE bookid = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookid);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Book_24133034> findByAuthorId(int authorId, int page, int pageSize) {
        List<Book_24133034> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT b.*, "
                   + "  a.author_name AS author_name, "
                   + "  (SELECT COUNT(*) FROM dbo.rating r WHERE r.bookid = b.bookid) AS review_count "
                   + "FROM dbo.books b "
                   + "INNER JOIN dbo.book_author ba ON b.bookid = ba.bookid "
                   + "INNER JOIN dbo.author a ON ba.author_id = a.author_id "
                   + "WHERE a.author_id = ? "
                   + "ORDER BY b.bookid ASC "
                   + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, authorId);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countByAuthorId(int authorId) {
        String sql = "SELECT COUNT(*) FROM dbo.book_author WHERE author_id = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, authorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Book_24133034 mapResultSetToBook(ResultSet rs) throws SQLException {
        Book_24133034 b = new Book_24133034();
        b.setBookid(rs.getInt("bookid"));
        b.setIsbn(rs.getInt("isbn"));
        b.setTitle(rs.getString("title"));
        b.setPublisher(rs.getString("publisher"));
        b.setPrice(rs.getBigDecimal("price"));
        b.setDescription(rs.getString("description"));
        b.setPublish_date(rs.getDate("publish_date"));
        b.setCover_image(rs.getString("cover_image"));
        b.setQuantity(rs.getInt("quantity"));

        try {
            b.setAuthor_name(rs.getString("author_name"));
        } catch (SQLException ignored) {}

        try {
            b.setReview_count(rs.getInt("review_count"));
        } catch (SQLException ignored) {}

        return b;
    }
}
