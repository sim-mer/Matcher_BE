package com.knu.matcher.controller;

import com.knu.matcher.annotation.TokenUserEmail;
import com.knu.matcher.dto.common.OffsetPagingResponse;
import com.knu.matcher.dto.request.CreateReservationPostDto;
import com.knu.matcher.dto.request.EditReservationPostDto;
import com.knu.matcher.dto.request.ReserveSeatDto;
import com.knu.matcher.dto.request.ReserveSeatsRequest;
import com.knu.matcher.dto.response.reservation.ReservationPostDetailDto;
import com.knu.matcher.dto.response.reservation.ReservationPostPagingDto;
import com.knu.matcher.service.ReservationPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservationpost")
public class ReservationPostController {
    private final ReservationPostService reservationPostService;

    @PostMapping
    public long createReservationPost(@RequestBody CreateReservationPostDto dto, @TokenUserEmail String email) {
        return reservationPostService.createReservationPost(dto, email);
    }

    @PutMapping("/{id}")
    public void updateReservationPost(@PathVariable long id, @RequestBody EditReservationPostDto dto) {
        reservationPostService.updateReservationPost(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteReservationPost(@PathVariable long id) {
        reservationPostService.deleteReservationPost(id);
    }

    @GetMapping("/{id}")
    public ReservationPostDetailDto getReservationPost(@PathVariable long id) {
        return reservationPostService.getReservationPostDetail(id);
    }

    @GetMapping
    public OffsetPagingResponse<ReservationPostPagingDto> getReservationPosts(@RequestParam int page, @RequestParam(required = false) String title) {
        return reservationPostService.getReservationPosts(page, 20, title);
    }

    @PostMapping("/{id}/seat")
    public void reserveSeat(@PathVariable long id, @RequestBody ReserveSeatDto dto, @TokenUserEmail String email) {
        reservationPostService.reserveSeat(id, dto, email);
    }

    @PostMapping("/{id}/seats")
    public void reserveSeats(@PathVariable long id, @RequestBody ReserveSeatsRequest dto, @TokenUserEmail String email) {
        reservationPostService.reserveSeats(id, dto, email);
    }

    @DeleteMapping("/{id}/seats")
    public void deleteReserveSeats(@PathVariable long id, @RequestBody ReserveSeatsRequest dto, @TokenUserEmail String email) {
        reservationPostService.deleteReserveSeats(id, dto, email);
    }
}
