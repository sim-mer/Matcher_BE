package com.knu.matcher.repository;

import com.knu.matcher.domain.jobpost.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final DataSource dataSource;
    private final CustomDataSourceUtils dataSourceUtils;


    public Long findLastCommentId(){
        Connection conn = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        String sql = "SELECT MAX(Cid) FROM COMMENTS";

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
    public Long createComment(Comment comment){
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO COMMENTS (Cid, Ccontent, Cdate, CJPid, CUemail) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, comment.getId());
            pstmt.setString(2, comment.getContent());
            pstmt.setTimestamp(3, Timestamp.valueOf(comment.getDate()));
            pstmt.setLong(4, comment.getJobPostId());
            pstmt.setString(5, comment.getUserEmail());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return comment.getId();
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
    public List<Comment> findCommentsByJobPostId(Long jobPostId){
        List<Comment> comments = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * from COMMENTS WHERE CJPid = ?";

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, jobPostId);
            rs = pstmt.executeQuery();
            while(rs.next()) {
                Long id = rs.getLong(1);
                String content = rs.getString(2);
                LocalDateTime date = rs.getTimestamp(3).toLocalDateTime();
                String userEmail = rs.getString(5);

                Comment comment = Comment.builder().id(id).content(content).date(date).jobPostId(jobPostId).userEmail(userEmail).build();
                comments.add(comment);
            }
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return comments;
    }

    public void deleteComment(Long commentId){
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM COMMENTS WHERE Cid = ?";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, commentId);

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
    
}
