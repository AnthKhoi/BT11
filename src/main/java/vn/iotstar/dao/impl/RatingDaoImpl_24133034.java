package vn.iotstar.dao.impl;

import vn.iotstar.connection.DBConnection_24133034;
import vn.iotstar.dao.IRatingDao_24133034;
import vn.iotstar.model.Rating_24133034;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingDaoImpl_24133034 implements IRatingDao_24133034 {

    @Override
    public List<Rating_24133034> findByBookId(int bookId) {
        List<Rating_24133034> list = new ArrayList<>();
        String sql = "SELECT r.*, u.fullname, u.email FROM dbo.rating r "
                   + "INNER JOIN dbo.users u ON r.userid = u.id "
                   + "WHERE r.bookid = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rating_24133034 rating = new Rating_24133034();
                    rating.setUserid(rs.getInt("userid"));
                    rating.setBookid(rs.getInt("bookid"));
                    rating.setRating(rs.getInt("rating"));
                    rating.setReview_text(rs.getString("review_text"));
                    rating.setUser_fullname(rs.getString("fullname"));
                    rating.setUser_email(rs.getString("email"));
                    list.add(rating);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int countByBookId(int bookId) {
        String sql = "SELECT COUNT(*) FROM dbo.rating WHERE bookid = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookId);
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

    @Override
    public boolean insert(Rating_24133034 rating) {
        // Nếu user đã đánh giá cuốn này rồi thì update, ngược lại insert
        String sql = "IF EXISTS (SELECT 1 FROM dbo.rating WHERE userid = ? AND bookid = ?) "
                   + "    UPDATE dbo.rating SET rating = ?, review_text = ? WHERE userid = ? AND bookid = ? "
                   + "ELSE "
                   + "    INSERT INTO dbo.rating (userid, bookid, rating, review_text) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rating.getUserid());
            ps.setInt(2, rating.getBookid());
            ps.setInt(3, rating.getRating());
            ps.setString(4, rating.getReview_text());
            ps.setInt(5, rating.getUserid());
            ps.setInt(6, rating.getBookid());
            ps.setInt(7, rating.getUserid());
            ps.setInt(8, rating.getBookid());
            ps.setInt(9, rating.getRating());
            ps.setString(10, rating.getReview_text());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkUserRated(int userId, int bookId) {
        String sql = "SELECT 1 FROM dbo.rating WHERE userid = ? AND bookid = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
