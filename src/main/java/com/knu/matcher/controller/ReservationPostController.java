//package com.knu.matcher.controller;
//
//import com.knu.matcher.dto.request.CreateReservationPostDto;
//import com.knu.matcher.dto.request.EditReservationPostDto;
//import com.knu.matcher.dto.response.reservation.ReservationPostDetailDto;
//import com.knu.matcher.dto.response.reservation.ReservationPostPagingDto;
//import com.knu.matcher.service.ReservationPostService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/reservationpost")
//public class ReservationPostController {
//    private final ReservationPostService reservationPostService;
//
//    @PostMapping
//    public long createReservationPost(@RequestBody CreateReservationPostDto dto) {
//        return reservationPostService.createReservationPost(dto);
//    }
//
//    @PutMapping("/{id}")
//    public void updateReservationPost(@PathVariable long id, @RequestBody EditReservationPostDto dto) {
//        reservationPostService.updateReservationPost(id, dto);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteReservationPost(@PathVariable long id) {
//        reservationPostService.deleteReservationPost(id);
//    }
//
//    @GetMapping("/{id}")
//    public ReservationPostDetailDto getReservationPost(@PathVariable long id) {
//        return reservationPostService.getReservationPostDetail(id);
//    }
//
//    @GetMapping
//    public ReservationPostPagingDto getReservationPosts(@RequestParam int page, @RequestParam(required = false) String title) {
//        return reservationPostService.getReservationPosts(page, title);
//    }
//
//}
