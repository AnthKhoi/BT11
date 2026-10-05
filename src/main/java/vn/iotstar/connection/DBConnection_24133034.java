package vn.iotstar.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection_24133034 {
    // Thay đổi thông tin kết nối phù hợp với máy của bạn
    private static final String SERVER_NAME = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE_NAME = "BookStore_24133034";
    private static final String USER_NAME = "sa";
    private static final String PASSWORD = "123"; // Hoặc mật khẩu sa của bạn

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=false;trustServerCertificate=true;characterEncoding=UTF-8",
                    SERVER_NAME, PORT, DATABASE_NAME);
            conn = DriverManager.getConnection(url, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
        return conn;
    }

    public static void main(String[] args) {
        Connection conn = DBConnection_24133034.getConnection();
        if (conn != null) {
            System.out.println("Kết nối cơ sở dữ liệu thành công!");
            try {
                conn.close();
            } catch (SQLException ignored) {}
        } else {
            System.out.println("Kết nối cơ sở dữ liệu thất bại!");
        }
    }
}
