package com.knu.matcher.repository;

import com.knu.matcher.domain.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRepository {
    private final DataSource dataSource;
    private final CustomDataSourceUtils dataSourceUtils;

    public Long findLastMessageId(){
        Connection conn = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        String sql = "SELECT MAX(Mid) FROM MESSAGE";

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
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
                if (rs != null) rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public List<String> findUsersEmailInChatByUserEmail(String userEmail){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "select distinct * from (" +
                "(select mremail as other from Message where msemail = ? )"
                +" UNION "
                +"(select msemail as other from Message where mremail = ? ) )";


        List<String> emails = new ArrayList<>();

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userEmail);
            pstmt.setString(2, userEmail);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String email = rs.getString(1);

                emails.add(email);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return emails;
    }

    public Long save(Message message) {
        Connection conn = null;
        PreparedStatement pstmt = null;


        String sql = "INSERT INTO MESSAGE VALUES " +
                "(?, ?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, message.getId());
            pstmt.setString(2, message.getContent());
            pstmt.setTimestamp(3, Timestamp.valueOf(message.getDate()));
            pstmt.setString(4, message.getSenderEmail());
            pstmt.setString(5, message.getReceiverEmail());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return message.getId();
            }
            conn.rollback();
            return null;
        }catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dataSourceUtils.close(conn, pstmt, null);
        }
        return null;
    }

    public Message findById(Long id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM MESSAGE WHERE Mid = ?";

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Long mid = rs.getLong(1);
                String content = rs.getString(2);
                LocalDateTime date = rs.getTimestamp(3).toLocalDateTime();
                String senderEmail = rs.getString(4);
                String receiverEmail = rs.getString(5);

                Message message = Message.builder().id(mid).content(content)
                        .date(date).senderEmail(senderEmail)
                        .receiverEmail(receiverEmail).build();

                return message;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }



    public List<Message> findMessagesByEmail(String userEmail,String otherUserEmail){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM MESSAGE WHERE (MSemail = ? AND MRemail = ?) OR (MSemail = ? AND MRemail = ?) ORDER BY Mdate DESC";

        List<Message> messages = new ArrayList<>();

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userEmail);
            pstmt.setString(2, otherUserEmail);
            pstmt.setString(3, otherUserEmail);
            pstmt.setString(4, userEmail);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Long mid = rs.getLong(1);
                String content = rs.getString(2);
                LocalDateTime date = rs.getTimestamp(3).toLocalDateTime();
                String senderEmail = rs.getString(4);
                String receiverEmail = rs.getString(5);

                Message message = Message.builder().id(mid).content(content)
                        .date(date).senderEmail(senderEmail)
                        .receiverEmail(receiverEmail).build();

                messages.add(message);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return messages;
    }

    public List<Message> findMessageBySenderEmail(String userEmail, Boolean isSender) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = null;

        if(isSender)
            sql = "SELECT * FROM RESERVATIONPOST WHERE MSemail = ?";
        else
            sql = "SELECT * FROM RESERVATIONPOST WHERE MRemail = ?";

        List<Message> messages = new ArrayList<>();

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userEmail);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Long mid = rs.getLong(1);
                String content = rs.getString(2);
                LocalDateTime date = rs.getTimestamp(3).toLocalDateTime();
                String senderEmail = rs.getString(4);
                String receiverEmail = rs.getString(5);

                Message message = Message.builder().id(mid).content(content)
                        .date(date).senderEmail(senderEmail)
                        .receiverEmail(receiverEmail).build();

                messages.add(message);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return messages;
    }
    public void delete(Long id) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM MESSAGE WHERE Mid = ?";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
            }
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dataSourceUtils.close(conn, pstmt, null);
        }
    }
}
