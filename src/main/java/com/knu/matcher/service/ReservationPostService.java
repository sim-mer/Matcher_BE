package com.knu.matcher.service;

import com.knu.matcher.domain.reservation.ReservationPost;
import com.knu.matcher.domain.reservation.Seat;
import com.knu.matcher.dto.request.CreateReservationPostDto;
import com.knu.matcher.dto.request.EditReservationPostDto;
import com.knu.matcher.dto.response.reservation.ReservationPostDetailDto;
import com.knu.matcher.repository.ReservationPostRepository;
import com.knu.matcher.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationPostService {
    private final ReservationPostRepository reservationPostRepository;
    private final SeatRepository seatRepository;

    public long createReservationPost(CreateReservationPostDto dto, String email) {
        long reservationPostId = reservationPostRepository.findLastId() + 1;
        long seatId = seatRepository.findLastId() + 1;

        List<CreateReservationPostDto.Seat> disableSeatList = dto.getDisableSeatList();

        ReservationPost reservationPost = ReservationPost.builder()
                .id(reservationPostId)
                .title(dto.getTitle())
                .content(dto.getContent())
                .date(LocalDateTime.now())
                .ownerEmail(email)
                .rowSize(dto.getRowSize())
                .colSize(dto.getColSize())
                .build();
        if(reservationPostRepository.save(reservationPost) == null) {
            throw new IllegalStateException("예약 게시글 생성에 실패하였습니다.");
        }

        for (int row = 0; row < dto.getRowSize(); row++) {
            for (int col = 0; col < dto.getColSize(); col++) {
                if (!isSeatDisabled(row, col, disableSeatList)) {
                    Seat seat = Seat.builder()
                            .id(seatId++)
                            .rowNumber(row)
                            .colNumber(col)
                            .build();
                    if (seatRepository.save(reservationPostId, seat) == null) {
                        throw new IllegalStateException("예약 게시글 생성에 실패하였습니다.");
                    }
                }
            }
        }

        return reservationPostId;
    }

    public void deleteReservationPost(long id) {
        if(!reservationPostRepository.delete(id)) {
            throw new IllegalStateException("예약 게시글 삭제에 실패하였습니다.");
        } //cascade라 seat도 같이 삭제됨
    }

    public void updateReservationPost(long id, EditReservationPostDto dto) {
        ReservationPost reservationPost = ReservationPost.builder()
                .id(id)
                .title(dto.getTitle())
                .content(dto.getContent())
                .date(LocalDateTime.now())
                .build();
        if(!reservationPostRepository.update(reservationPost)) {
            throw new IllegalStateException("예약 게시글 수정에 실패하였습니다.");
        }
    }

    private boolean isSeatDisabled(int row, int col, List<CreateReservationPostDto.Seat> disableSeatList) {
        return disableSeatList.stream()
                .anyMatch(disableSeat -> disableSeat.getRowNumber() == row
                        && disableSeat.getColNumber() == col);
    }
}
