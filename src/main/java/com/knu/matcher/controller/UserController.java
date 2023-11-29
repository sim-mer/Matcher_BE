package com.knu.matcher.controller;

import com.knu.matcher.annotation.TokenUserEmail;
import com.knu.matcher.dto.common.OffsetPagingResponse;
import com.knu.matcher.dto.jobpost.JobPostSummaryDto;
import com.knu.matcher.dto.response.reservation.ReservationPostPagingDto;
import com.knu.matcher.dto.user.EditUserInfoRequest;
import com.knu.matcher.dto.user.UserInfoDto;
import com.knu.matcher.service.JobPostService;
import com.knu.matcher.service.ReservationPostService;
import com.knu.matcher.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ReservationPostService reservationPostService;
    private final JobPostService jobPostService;

    @GetMapping
    public UserInfoDto getUser(@TokenUserEmail String userEmail) {
        return userService.getUser(userEmail);
    }

    @PutMapping
    public void updateUser(@RequestBody @Valid EditUserInfoRequest dto, @TokenUserEmail String userEmail) {
        userService.updateUser(dto, userEmail);
    }

    @DeleteMapping
    public void deleteUser(@TokenUserEmail String userEmail) {
        userService.deleteUser(userEmail);
    }

    @GetMapping("/reservationpost")
    public OffsetPagingResponse<ReservationPostPagingDto> getMyReservationPosts(@RequestParam int page, @TokenUserEmail String email) {
        return reservationPostService.getMyReservationPosts(page, 20, email);
    }

    @GetMapping("/jobpost")
    public OffsetPagingResponse<JobPostSummaryDto> getMyJobPosts(@RequestParam int page, @RequestParam(value = "title",required = false) String title,@TokenUserEmail String email) {
        if(title == null) title = "";
        return jobPostService.getJobPostSummaryList(page, 20, title, email);
    }
}
