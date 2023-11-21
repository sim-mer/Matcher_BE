package com.knu.matcher.repository;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CustomDataSourceUtils {
    protected void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
