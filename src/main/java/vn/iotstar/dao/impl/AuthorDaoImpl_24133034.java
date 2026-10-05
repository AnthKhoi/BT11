package vn.iotstar.dao.impl;

import vn.iotstar.connection.DBConnection_24133034;
import vn.iotstar.dao.IAuthorDao_24133034;
import vn.iotstar.model.Author_24133034;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDaoImpl_24133034 implements IAuthorDao_24133034 {

    @Override
    public List<Author_24133034> findAll() {
        List<Author_24133034> list = new ArrayList<>();
        String sql = "SELECT * FROM dbo.author ORDER BY author_name ASC";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToAuthor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Author_24133034 findById(int id) {
        String sql = "SELECT * FROM dbo.author WHERE author_id = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAuthor(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Author_24133034> findByBookId(int bookId) {
        List<Author_24133034> list = new ArrayList<>();
        String sql = "SELECT a.* FROM dbo.author a "
                   + "INNER JOIN dbo.book_author ba ON a.author_id = ba.author_id "
                   + "WHERE ba.bookid = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAuthor(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean insert(Author_24133034 author) {
        String sql = "INSERT INTO dbo.author (author_name, date_of_birth) VALUES (?, ?)";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, author.getAuthor_name());
            ps.setDate(2, author.getDate_of_birth());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addAuthorToBook(int bookId, int authorId) {
        String sql = "INSERT INTO dbo.book_author (bookid, author_id) VALUES (?, ?)";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setInt(2, authorId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeAuthorsByBookId(int bookId) {
        String sql = "DELETE FROM dbo.book_author WHERE bookid = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Author_24133034 mapResultSetToAuthor(ResultSet rs) throws SQLException {
        Author_24133034 a = new Author_24133034();
        a.setAuthor_id(rs.getInt("author_id"));
        a.setAuthor_name(rs.getString("author_name"));
        a.setDate_of_birth(rs.getDate("date_of_birth"));
        return a;
    }
}
