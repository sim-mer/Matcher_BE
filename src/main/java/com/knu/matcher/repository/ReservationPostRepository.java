package com.knu.matcher.repository;

import com.knu.matcher.domain.reservation.ReservationPost;
import com.knu.matcher.dto.response.reservation.ReservationPostPagingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public ReservationPost findById(long id) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "SELECT * FROM RESERVATIONPOST WHERE RPid = ?";
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();
            if(rs.next()) {
                Clob clob = rs.getClob(3);
                String content = clobToString(clob);
                ReservationPost reservationPost = ReservationPost.builder()
                        .id(id)
                        .title(rs.getString(2))
                        .content(content)
                        .date(rs.getTimestamp(4).toLocalDateTime())
                        .ownerEmail(rs.getString(5))
                        .rowSize(rs.getInt(6))
                        .colSize(rs.getInt(7))
                        .build();
                return reservationPost;
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

    public List<ReservationPostPagingDto> findByTitleWithPage(int page, int pageSize, String title) {

        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "SELECT * " +
                "FROM (" +
                "    SELECT ROWNUM AS RNUM, RP.* " +
                "    FROM (" +
                "        SELECT RP.RPid, RP.RPtitle, RP.RPdate, U.Name" +
                "        FROM RESERVATIONPOST RP" +
                "        JOIN USERS U ON RP.RPUemail = U.Email" +
                "        WHERE RPtitle LIKE ? " +
                "        ORDER BY RPdate DESC" +
                "    ) RP" +
                ") " +
                "WHERE RNUM BETWEEN ? AND ?";
        ResultSet rs = null;

        List<ReservationPostPagingDto> dtoList = new ArrayList<>();

        try {
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + title + "%");
            pstmt.setInt(2, page * pageSize + 1);
            pstmt.setInt(3, (page + 1) * pageSize);

            rs = pstmt.executeQuery();

            while(rs.next()) {
                ReservationPostPagingDto dto = ReservationPostPagingDto.builder()
                        .id(rs.getLong(2))
                        .title(rs.getString(3))
                        .date(rs.getTimestamp(4).toLocalDateTime())
                        .ownerName(rs.getString(5))
                        .build();
                dtoList.add(dto);
            }
            return dtoList;
        }catch(Exception ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }

    public Long contByTitle(String title) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "SELECT COUNT(*) FROM RESERVATIONPOST WHERE RPtitle LIKE ?";
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
}
