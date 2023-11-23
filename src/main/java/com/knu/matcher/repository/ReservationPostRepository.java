package com.knu.matcher.repository;

import com.knu.matcher.domain.reservation.ReservationPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
@RequiredArgsConstructor
public class ReservationPostRepository {
    private final DataSource dataSource;
    private final CustomDataSourceUtils dataSourceUtils;

    public Long findLastId(){
        Connection conn = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        String sql = "SELECT MAX(RPid) FROM RESERVATIONPOST";

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

    public Long save(ReservationPost reservationPost) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO RESERVATIONPOST VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, reservationPost.getId());
            pstmt.setString(2, reservationPost.getTitle());

            Clob contentClob = conn.createClob();
            contentClob.setString(1, reservationPost.getContent());

            pstmt.setClob(3, contentClob);
            pstmt.setTimestamp(4, Timestamp.valueOf(reservationPost.getDate()));
            pstmt.setString(5, reservationPost.getOwnerEmail());
            pstmt.setInt(6, reservationPost.getRowSize());
            pstmt.setInt(7, reservationPost.getColSize());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return reservationPost.getId();
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

        String sql = "DELETE FROM RESERVATIONPOST WHERE RPid = ?";

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

    public boolean update(ReservationPost reservationPost) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE RESERVATIONPOST SET RPtitle = ?, RPcontent = ?, RPdate = ? WHERE RPid = ?";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reservationPost.getTitle());

            Clob contentClob = conn.createClob();
            contentClob.setString(1, reservationPost.getContent());

            pstmt.setClob(2, contentClob);
            pstmt.setTimestamp(3, Timestamp.valueOf(reservationPost.getDate()));
            pstmt.setLong(4, reservationPost.getId());

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
}
