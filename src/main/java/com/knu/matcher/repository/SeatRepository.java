package com.knu.matcher.repository;

import com.knu.matcher.dto.request.CreateReservationPostDto;
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
                } catch (Exception ex) {
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

    public boolean saveSeatList(List<CreateReservationPostDto.Seat> disableSeats, int rowSize, int colSize, long reservationPostId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO SEAT VALUES (?, ?, ?, ?)";

        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            pstmt = conn.prepareStatement(sql);

            for (int row = 0; row < rowSize; row++) {
                for (int col = 0; col < colSize; col++) {
                    if (!isSeatDisabled(row, col, disableSeats)) {
                        pstmt.setLong(1, getNewSeatId());
                        pstmt.setInt(2, row);
                        pstmt.setInt(3, col);
                        pstmt.setLong(4, reservationPostId);
                        pstmt.addBatch();
                    }
                }
            }

            int[] rowsAffected = pstmt.executeBatch();

            for (int affectedRows : rowsAffected) {
                if (affectedRows < 0) {
                    conn.rollback();
                    return false;
                }
            }
            conn.commit();
            return true;
        }catch(SQLException ex2) {
            ex2.printStackTrace();
        }finally {
            dataSourceUtils.close(conn, pstmt, null);
        }
        return false;
    }

    private boolean isSeatDisabled(int row, int col, List<CreateReservationPostDto.Seat> disableSeatList) {
        return disableSeatList.stream()
                .anyMatch(disableSeat -> disableSeat.getRowNumber() == row
                        && disableSeat.getColNumber() == col);
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
