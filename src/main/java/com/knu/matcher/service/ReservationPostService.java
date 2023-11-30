package com.knu.matcher.service;

import com.knu.matcher.domain.reservation.ReservationPost;
import com.knu.matcher.domain.reservation.Seat;
import com.knu.matcher.domain.user.User;
import com.knu.matcher.dto.common.OffsetPagingResponse;
import com.knu.matcher.dto.request.CreateReservationPostDto;
import com.knu.matcher.dto.request.EditReservationPostDto;
import com.knu.matcher.dto.request.ReserveSeatDto;
import com.knu.matcher.dto.request.ReserveSeatsRequest;
import com.knu.matcher.dto.response.reservation.AuthorDto;
import com.knu.matcher.dto.response.reservation.ReservationPostDetailDto;
import com.knu.matcher.dto.response.reservation.ReservationPostPagingDto;
import com.knu.matcher.repository.ReservationPostRepository;
import com.knu.matcher.repository.SeatRepository;
import com.knu.matcher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationPostService {
    private final ReservationPostRepository reservationPostRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    public long createReservationPost(CreateReservationPostDto dto, String email) {
        List<CreateReservationPostDto.Seat> disableSeatList = dto.getDisableSeatList();

        Long id = reservationPostRepository.getRPid();

        ReservationPost reservationPost = ReservationPost.builder()
                .id(id)
                .title(dto.getTitle())
                .content(dto.getContent())
                .date(LocalDateTime.now())
                .ownerEmail(email)
                .rowSize(dto.getRowSize())
                .colSize(dto.getColSize())
                .build();


        if((id = reservationPostRepository.save(reservationPost)) == null) {
            throw new IllegalStateException("예약 게시글 생성에 실패하였습니다.");
        }

        for (int row = 0; row < dto.getRowSize(); row++) {
            for (int col = 0; col < dto.getColSize(); col++) {
                if (!isSeatDisabled(row, col, disableSeatList)) {
                    Seat seat = Seat.builder()
                            .id(seatRepository.getNewSeatId())
                            .rowNumber(row)
                            .colNumber(col)
                            .reservationPostId(id)
                            .build();
                    if (seatRepository.save(seat) == null) {
                        throw new IllegalStateException("예약 좌석 생성에 실패하였습니다.");
                    }
                }
            }
        }
        return id;
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

    public ReservationPostDetailDto getReservationPostDetail(long id) {
        ReservationPost reservationPost = reservationPostRepository.findById(id);
        if(reservationPost == null) {
            throw new IllegalStateException("예약 게시글이 존재하지 않습니다.");
        }
        User user = userRepository.findByEmail(reservationPost.getOwnerEmail());

        AuthorDto author = AuthorDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .major(user.getMajor())
                .stdNumber(user.getStdNumber())
                .build();

        List<ReservationPostDetailDto.Seat> seatList = seatRepository.findByRPidWithRU(id);

        return ReservationPostDetailDto.builder()
                .id(reservationPost.getId())
                .title(reservationPost.getTitle())
                .content(reservationPost.getContent())
                .date(reservationPost.getDate())
                .author(author)
                .rowSize(reservationPost.getRowSize())
                .colSize(reservationPost.getColSize())
                .seatList(seatList)
                .build();
    }

    private boolean isSeatDisabled(int row, int col, List<CreateReservationPostDto.Seat> disableSeatList) {
        return disableSeatList.stream()
                .anyMatch(disableSeat -> disableSeat.getRowNumber() == row
                        && disableSeat.getColNumber() == col);
    }

    public OffsetPagingResponse<ReservationPostPagingDto> getReservationPosts(int page, int pageSize, String title) {
        List<ReservationPostPagingDto> reservationPostPagingDtoList = reservationPostRepository.findByTitleWithPage(page, pageSize, title);
        if(reservationPostPagingDtoList == null) {
            throw new IllegalStateException("예약 게시글 조회에 실패하였습니다.");
        }
        if(reservationPostPagingDtoList.isEmpty()) {
            return new OffsetPagingResponse<>(false, reservationPostPagingDtoList);
        }
        Long total = contByTitle(title);
        boolean hasNext = !(total <= (page + 1) * pageSize);
        return new OffsetPagingResponse<>(hasNext, reservationPostPagingDtoList);
    }

    private Long contByTitle(String title) {
        return reservationPostRepository.contByTitle(title);
    }

    public void reserveSeat(long id, ReserveSeatDto dto, String email) {
        if(!reservationPostRepository.reserveSeat(id, dto.getRowNumber(), dto.getColNumber(), email)){
            throw new IllegalStateException("예약에 실패하였습니다.");
        }
    }

    public OffsetPagingResponse<ReservationPostPagingDto> getMyReservationPosts(int page, int pageSize, String email) {
        List<ReservationPostPagingDto> reservationPostPagingDtoList = reservationPostRepository.findByEmailWithPage(page, pageSize, email);
        if(reservationPostPagingDtoList == null) {
            throw new IllegalStateException("예약 게시글 조회에 실패하였습니다.");
        }
        if(reservationPostPagingDtoList.isEmpty()) {
            return new OffsetPagingResponse<>(false, reservationPostPagingDtoList);
        }
        Long total = reservationPostRepository.countByEmail(email);
        boolean hasNext = !(total <= (page + 1) * pageSize);
        return new OffsetPagingResponse<>(hasNext, reservationPostPagingDtoList);
    }

    public void reserveSeats(long id, ReserveSeatsRequest dto, String email) {
        if(!seatRepository.reserveSeatList(id, dto.getSeatList(), email)){
            throw new IllegalStateException("예약에 실패하였습니다.");
        }
    }
}
