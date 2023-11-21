package com.knu.matcher.repository;

import com.knu.matcher.domain.jobpost.JobPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class JobPostRepository {
    private final DataSource dataSource;
    private final CustomDataSourceUtils dataSourceUtils;
    public Long findLastJobPostId(){
        Connection conn = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        String sql = "SELECT MAX(JPid) FROM JOBPOST";

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
    public List<JobPost> findPostByUserEmail(String userEmail) {
        List<JobPost> jobPosts = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * from JOBPOST WHERE JPUemail = ?";

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userEmail);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                Long id = rs.getLong(1);
                String title = rs.getString(2);
                String content = rs.getString(3);
                LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();

                JobPost jobPost = JobPost.builder().id(id).title(title).content(content).date(date).userEmail(userEmail).build();
                jobPosts.add(jobPost);
            }
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }


        return jobPosts;
    }

    public List<JobPost> findPostsByTitleContains(String searchTitle) {
        List<JobPost> jobPosts = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * from JOBPOST WHERE  JPtitle LIKE ?";

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchTitle + "%");
            rs = pstmt.executeQuery();
            while(rs.next()) {
                Long id = rs.getLong(1);
                String title = rs.getString(2);
                String content = rs.getString(3);
                LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();
                String userEmail = rs.getString(5);

                JobPost jobPost = JobPost.builder().id(id).title(title).content(content).date(date).userEmail(userEmail).build();
                jobPosts.add(jobPost);
            }
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }


        return jobPosts;
    }
    public List<JobPost> findPostsByLatest(int count){
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "SELECT * FROM (SELECT * FROM JOBPOST ORDER BY JPdate) WHERE ROWNUM <= ?";
        List<JobPost> jobPosts = new ArrayList<>();
        ResultSet rs = null;


        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, count);

            rs = pstmt.executeQuery();
            while(rs.next()) {
                Long id = rs.getLong(1);
                String title = rs.getString(2);
                String content = rs.getString(3);
                LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();
                String userEmail = rs.getString(5);

                JobPost jobPost = JobPost.builder().id(id).title(title).content(content).date(date).userEmail(userEmail).build();
                jobPosts.add(jobPost);
            }

        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return jobPosts;
    }
    public JobPost findPostById(Long Id){
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "SELECT * FROM JOBPOST WHERE JPid = ?";
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, Id);

            rs = pstmt.executeQuery();
            if(rs.next()) {
                Long id = rs.getLong(1);
                String title = rs.getString(2);
                String content = rs.getString(3);
                LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();
                String userEmail = rs.getString(5);
                JobPost jobPost = JobPost.builder().id(id).title(title).content(content).date(date).userEmail(userEmail).build();
                return jobPost;
            }
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }

    public boolean updateJobPost(JobPost jobPost) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE JOBPOST SET JPtitle = ?, JPcontent = ?, JPdate = ? WHERE JPid = ?";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jobPost.getTitle());
            pstmt.setString(2, jobPost.getContent());
            pstmt.setTimestamp(3, Timestamp.valueOf(jobPost.getDate()));
            pstmt.setLong(4, jobPost.getId());

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

    public boolean deletePost(Long id) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM JOBPOST WHERE JPid = ?";

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

    public Long save(JobPost jobPost) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO JOBPOST (JPid, JPtitle, JPcontent, JPdate, JPUemail) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, jobPost.getId());
            pstmt.setString(2, jobPost.getTitle());
            pstmt.setString(3, jobPost.getContent());
            pstmt.setTimestamp(4, Timestamp.valueOf(jobPost.getDate()));
            pstmt.setString(5, jobPost.getUserEmail());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return jobPost.getId();
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
}
