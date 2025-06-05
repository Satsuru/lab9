package com.example.pharmacy;

import java.sql.*;

public class DBConnection {
    private Connection conn;

    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/pharmacy", "root", "MortikSQL11"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void close(ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
    }

    public void close(Statement st) {
        try { if (st != null) st.close(); } catch (Exception ignored) {}
    }

    public void destroy() {
        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
    }
}
