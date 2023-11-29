package com.knu.matcher.repository;

import com.knu.matcher.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@RequiredArgsConstructor
@Repository
public class UserRepository {
    private final DataSource dataSource;
    private final CustomDataSourceUtils dataSourceUtils;

    public User findByEmail(String Email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM USERS WHERE Email = ?";

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String email = rs.getString(1);
                String password = rs.getString(2);
                String name = rs.getString(3);
                String major = rs.getString(4);
                String stdNumber = rs.getString(5);

                User user = User.builder().email(email).password(password)
                        .name(name).major(major).stdNumber(stdNumber)
                        .build();
                return user;
            }
        } catch (SQLException ex2) {
            ex2.printStackTrace();
        } finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }

    public boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE USERS SET Name = ?, Major = ?, std_number = ? WHERE Email = ?";
//        String sql = "UPDATE USERS SET Password = ?, Name = ?, Major = ?, std_number = ? WHERE Email = ?";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getMajor());
            pstmt.setString(3, user.getStdNumber());
            pstmt.setString(4, user.getEmail());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, null);
        }
        return false;
    }

    public boolean deleteUser(String Email) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM USERS WHERE Email = ?";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Email);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
            return false;
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, null);
        }
        return false;
    }


    public boolean save(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getMajor());
            pstmt.setString(5, user.getStdNumber());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return true;
            }
            conn.rollback();
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, null);
        }
        return false;
    }
}
