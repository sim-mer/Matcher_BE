package com.knu.matcher.repository;

import com.knu.matcher.domain.jobpost.JobPost;
import com.knu.matcher.domain.jobpost.JobPostSummaryWithUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class JobPostRepository {
    private final DataSource dataSource;
    private final CustomDataSourceUtils dataSourceUtils;
    public Long getNewJobPostId() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectSql = "SELECT JPid FROM ID FOR UPDATE";
        String updateSql = "UPDATE ID SET JPid = ?";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            pstmt = conn.prepareStatement(selectSql);
            while (true) {
                try {
                    rs = pstmt.executeQuery();
                    break;
                } catch (SQLException ex) {
                    if (ex.getErrorCode() == 8177) continue;
                    ex.printStackTrace();
                    break;
                }
            }

            Long currentId = null;
            if (rs.next()) {
                currentId = rs.getLong(1);
            }

            Long nextId = currentId + 1;

            pstmt = conn.prepareStatement(updateSql);
            pstmt.setLong(1, nextId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return currentId;
            }
            conn.rollback();
            return null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
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
    public JobPost findById(Long Id){
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

                Clob clob = rs.getClob(3);
                String content = clobToString(clob);

                Long id = rs.getLong(1);
                String title = rs.getString(2);
                LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();
                String userEmail = rs.getString(5);
                JobPost jobPost = JobPost.builder().id(id).title(title).content(content).date(date).userEmail(userEmail).build();
                return jobPost;
            }
        }catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }

    public void updateJobPost(JobPost jobPost) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE JOBPOST SET JPtitle = ?, JPcontent = ?, JPdate = ? WHERE JPid = ?";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            Clob contentClob = conn.createClob();
            contentClob.setString(1, jobPost.getContent());

            pstmt.setString(1, jobPost.getTitle());
            pstmt.setClob(2, contentClob);
            pstmt.setTimestamp(3, Timestamp.valueOf(jobPost.getDate()));
            pstmt.setLong(4, jobPost.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return;
            }
            conn.rollback();
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, null);
        }
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
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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
        ResultSet rs = null;


        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            Clob contentClob = conn.createClob();
            contentClob.setString(1, jobPost.getContent());

            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, jobPost.getId());
            pstmt.setString(2, jobPost.getTitle());
            pstmt.setClob(3, contentClob);
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
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }

    public List<JobPostSummaryWithUser> findJobPostSummaryList(int page, int pageSize, String titleKeyword) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "SELECT T2.* FROM (" +
                "SELECT T.*,(ROWNUM) ROW_NUM FROM (" +
                "SELECT JPid, JPTitle, JPdate, JPUEmail, Name, Major, Std_number FROM JOBPOST JOIN USERS ON USERS.Email = JPUemail " +
                "WHERE JPTITLE LIKE ? ORDER BY JPdate DESC" +
                ") T WHERE ROWNUM < ? " +
                ") T2 WHERE ROW_NUM >= ? ";


        List<JobPostSummaryWithUser> jobPostSummaryWithUsers = new ArrayList<>();
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + titleKeyword + "%");
            pstmt.setInt(2, (page + 1)*pageSize);
            pstmt.setInt(3, page*pageSize);

            rs = pstmt.executeQuery();
            while(rs.next()) {
                Long id = rs.getLong(1);
                String title = rs.getString(2);
                LocalDateTime date = rs.getTimestamp(3).toLocalDateTime();
                String userEmail = rs.getString(4);
                String userName = rs.getString(5);
                String userMajor = rs.getString(6);
                String userStdNumber = rs.getString(7);

                JobPostSummaryWithUser jobPostSummaryWithUser = JobPostSummaryWithUser.builder()
                        .id(id).title(title).date(date).email(userEmail).name(userName).major(userMajor).stdNumber(userStdNumber).build();
                jobPostSummaryWithUsers.add(jobPostSummaryWithUser);

            }
            return jobPostSummaryWithUsers;
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return jobPostSummaryWithUsers;
    }

    public List<JobPostSummaryWithUser> findJobPostSummaryList(int page, int pageSize, String titleKeyword, String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "SELECT T2.* FROM (" +
                "SELECT T.*,(ROWNUM) ROW_NUM FROM (" +
                "SELECT JPid, JPTitle, JPdate, JPUEmail, Name, Major, Std_number FROM JOBPOST JOIN USERS ON USERS.Email = JPUemail " +
                "WHERE JPTITLE LIKE ? AND JPUEmail = ? ORDER BY JPdate DESC" +
                ") T WHERE ROWNUM < ? " +
                ") T2 WHERE ROW_NUM >= ? ";


        List<JobPostSummaryWithUser> jobPostSummaryWithUsers = new ArrayList<>();
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + titleKeyword + "%");
            pstmt.setString(2, email);
            pstmt.setInt(3, (page + 1)*pageSize);
            pstmt.setInt(4, page*pageSize);

            rs = pstmt.executeQuery();
            while(rs.next()) {
                Long id = rs.getLong(1);
                String title = rs.getString(2);
                LocalDateTime date = rs.getTimestamp(3).toLocalDateTime();
                String userEmail = rs.getString(4);
                String userName = rs.getString(5);
                String userMajor = rs.getString(6);
                String userStdNumber = rs.getString(7);

                JobPostSummaryWithUser jobPostSummaryWithUser = JobPostSummaryWithUser.builder()
                        .id(id).title(title).date(date).email(userEmail).name(userName).major(userMajor).stdNumber(userStdNumber).build();
                jobPostSummaryWithUsers.add(jobPostSummaryWithUser);

            }
            return jobPostSummaryWithUsers;
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return jobPostSummaryWithUsers;
    }

    public Long getCountByTitle(String title) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "SELECT COUNT(*) FROM JOBPOST WHERE JPTitle LIKE ?";
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + title + "%");

            rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getLong(1);
            }
        }catch(Exception ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }
    private String clobToString(Clob clob) throws SQLException, IOException {
        StringBuilder sb = new StringBuilder();
        try (Reader reader = clob.getCharacterStream();
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}
