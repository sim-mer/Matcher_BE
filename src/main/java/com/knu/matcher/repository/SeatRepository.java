package com.knu.matcher.repository;

import com.knu.matcher.domain.reservation.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class SeatRepository {
    private final DataSource dataSource;
    private final CustomDataSourceUtils dataSourceUtils;

    public Long findLastId(){
        Connection conn = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        String sql = "SELECT MAX(Sid) FROM SEAT";

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

    public Long save(long reservationPostId, Seat seat) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO SEAT VALUES (?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, seat.getId());
            pstmt.setInt(2, seat.getRowNumber());
            pstmt.setInt(3, seat.getColNumber());
            pstmt.setLong(4, reservationPostId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                conn.commit();
                return seat.getId();
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
