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


}
