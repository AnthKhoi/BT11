package vn.iotstar.dao.impl;

import vn.iotstar.connection.DBConnection_24133034;
import vn.iotstar.dao.IUserDao_24133034;
import vn.iotstar.model.User_24133034;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl_24133034 implements IUserDao_24133034 {

    @Override
    public boolean insert(User_24133034 user) {
        String sql = "INSERT INTO dbo.users (email, fullname, phone, passwd, signup_date, is_admin, is_active, otp_code) "
                   + "VALUES (?, ?, ?, ?, GETDATE(), ?, ?, ?)";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullname());
            ps.setInt(3, user.getPhone());
            ps.setString(4, user.getPasswd());
            ps.setBoolean(5, user.isIs_admin());
            ps.setBoolean(6, user.isIs_active());
            ps.setString(7, user.getOtp_code());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User_24133034 user) {
        String sql = "UPDATE dbo.users SET fullname = ?, phone = ?, passwd = ?, is_admin = ?, is_active = ? WHERE id = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullname());
            ps.setInt(2, user.getPhone());
            ps.setString(3, user.getPasswd());
            ps.setBoolean(4, user.isIs_admin());
            ps.setBoolean(5, user.isIs_active());
            ps.setInt(6, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateOtp(String email, String otp) {
        String sql = "UPDATE dbo.users SET otp_code = ? WHERE email = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, otp);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean activateAccount(String email) {
        String sql = "UPDATE dbo.users SET is_active = 1, otp_code = NULL WHERE email = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateLastLogin(int userId) {
        String sql = "UPDATE dbo.users SET last_login = GETDATE() WHERE id = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User_24133034 findById(int id) {
        String sql = "SELECT * FROM dbo.users WHERE id = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User_24133034 findByEmail(String email) {
        String sql = "SELECT * FROM dbo.users WHERE email = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User_24133034 checkLogin(String email, String password) {
        String sql = "SELECT * FROM dbo.users WHERE email = ? AND passwd = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkExistEmail(String email) {
        String sql = "SELECT id FROM dbo.users WHERE email = ?";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<User_24133034> findAll() {
        List<User_24133034> list = new ArrayList<>();
        String sql = "SELECT * FROM dbo.users";
        try (Connection conn = DBConnection_24133034.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private User_24133034 mapResultSetToUser(ResultSet rs) throws SQLException {
        User_24133034 user = new User_24133034();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setFullname(rs.getString("fullname"));
        user.setPhone(rs.getInt("phone"));
        user.setPasswd(rs.getString("passwd"));
        user.setSignup_date(rs.getTimestamp("signup_date"));
        user.setLast_login(rs.getTimestamp("last_login"));
        user.setIs_admin(rs.getBoolean("is_admin"));
        user.setIs_active(rs.getBoolean("is_active"));
        user.setOtp_code(rs.getString("otp_code"));
        return user;
    }
}
