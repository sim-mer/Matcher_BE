package com.knu.matcher.repository;

import com.knu.matcher.domain.jobpost.JobPostImg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
@RequiredArgsConstructor
public class JobPostImgRepository {
    private final DataSource dataSource;
    private final CustomDataSourceUtils dataSourceUtils;

    public Long findLastJobPostImgId(){
        Connection conn = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        String sql = "SELECT MAX(JPIid) FROM JOBPOSTIMG";

        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while(rs.next()){
                return rs.getLong(1);
            }
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }

    public Long save(JobPostImg jobPostImg) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO JOBPOSTIMG VALUES (?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, jobPostImg.getId());
            pstmt.setString(2, jobPostImg.getName());
            pstmt.setLong(3, jobPostImg.getJobPostId());
            pstmt.setString(4, jobPostImg.getUrl());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return jobPostImg.getId();
            }
            conn.rollback();
            return null;
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, null);
        }
        return null;
    }

    public boolean delete(long id) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM JOBPOSTIMG WHERE JPIid = ?";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

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

    public String findUUIDname(long imageId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        String sql = "SELECT URL, JPIname FROM JOBPOSTIMG WHERE JPIid = ?";

        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, imageId);
            rs = pstmt.executeQuery();

            while(rs.next()){
                String uuid =  rs.getString(1);
                String name = rs.getString(2);
                return uuid + "_" + name;
            }
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }
}
