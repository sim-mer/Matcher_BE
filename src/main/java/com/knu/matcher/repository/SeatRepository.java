package com.knu.matcher.repository;

import com.knu.matcher.domain.reservation.Seat;
import com.knu.matcher.dto.response.reservation.ReservationPostDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

            if(rs.next()){
                return rs.getLong(1);
            }
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }

    public Long save(Seat seat) {
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
            pstmt.setLong(4, seat.getReservationPostId());

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

    public List<ReservationPostDetailDto.Seat> findByRPidWithRU(long id) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        ResultSet rs = null;

        String sql = "SELECT S.Rownumber, S.Columnnumber, R.RUemail " +
                "FROM SEAT S " +
                "LEFT JOIN RESERVATION R ON S.Sid = R.RSid " +
                "WHERE S.SRPid = ?";

        List<ReservationPostDetailDto.Seat> seatList = new ArrayList<>();

        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            while(rs.next()){
                ReservationPostDetailDto.Seat seat = ReservationPostDetailDto.Seat.builder()
                        .rowNumber(rs.getInt(1))
                        .colNumber(rs.getInt(2))
                        .booker(rs.getString(3))
                        .build();
                seatList.add(seat);
            }
            return seatList;
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return null;
    }
}
