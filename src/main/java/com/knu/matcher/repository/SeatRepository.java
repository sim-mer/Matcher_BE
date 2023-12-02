package com.knu.matcher.repository;

import com.knu.matcher.domain.reservation.Seat;
import com.knu.matcher.dto.request.ReserveSeatDto;
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

    public Long getNewSeatId() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String selectSql = "SELECT Sid FROM ID FOR UPDATE";
        String updateSql = "UPDATE ID SET Sid = ?";

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

    public Long save(Seat seat) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO SEAT VALUES (?, ?, ?, ?)";

        try{
            conn = dataSource.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, seat.getId());
            pstmt.setInt(2, seat.getRowNumber());
            pstmt.setInt(3, seat.getColNumber());
            pstmt.setLong(4, seat.getReservationPostId());

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0){
                return seat.getId();
            }
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

    public boolean reserveSeatList(long reservationPostId, List<ReserveSeatDto> seats, String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "SELECT * FROM SEAT WHERE SRPid = ?";
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, reservationPostId);

            rs = pstmt.executeQuery();
            List<Long> seatIdList = new ArrayList<>();
            while (rs.next()){
                Long seatId = rs.getLong(1);
                int rowNumber = rs.getInt(2);
                int colNumber = rs.getInt(3);
                for(ReserveSeatDto seat: seats){
                    if(seat.getRowNumber() == rowNumber && seat.getColNumber() == colNumber){
                        seatIdList.add(seatId);
                    }
                }
            }

            sql = "INSERT INTO RESERVATION VALUES (?, ?)";
            pstmt = conn.prepareStatement(sql);
            for(Long rsId: seatIdList){
                pstmt.setString(1, email);
                pstmt.setLong(2, rsId);
                pstmt.addBatch();
            }

            int[] rowsAffected = pstmt.executeBatch();
            for (int affectedRows : rowsAffected) {
                if (affectedRows <= 0) {
                    conn.rollback();
                    return false;
                }
            }
            conn.commit();
            return true;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return false;
    }

    public boolean deleteSeatList(long reservationPostId, List<ReserveSeatDto> seats, String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "SELECT * FROM SEAT WHERE SRPid = ?";
        ResultSet rs = null;

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, reservationPostId);

            rs = pstmt.executeQuery();
            List<Long> seatIdList = new ArrayList<>();
            while (rs.next()){
                Long seatId = rs.getLong(1);
                int rowNumber = rs.getInt(2);
                int colNumber = rs.getInt(3);
                for(ReserveSeatDto seat: seats){
                    if(seat.getRowNumber() == rowNumber && seat.getColNumber() == colNumber){
                        seatIdList.add(seatId);
                    }
                }
            }

            sql = "DELETE FROM RESERVATION WHERE RUemail = ? AND RSid = ?";
            pstmt = conn.prepareStatement(sql);
            for(Long rsId: seatIdList){
                pstmt.setString(1, email);
                pstmt.setLong(2, rsId);
                pstmt.addBatch();
            }

            int[] rowsAffected = pstmt.executeBatch();
            for (int affectedRows : rowsAffected) {
                if (affectedRows <= 0) {
                    conn.rollback();
                    return false;
                }
            }
            conn.commit();
            return true;
        } catch (Exception ex2) {
            ex2.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            dataSourceUtils.close(conn, pstmt, rs);
        }
        return false;
    }
}
