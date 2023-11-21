package com.knu.matcher.dto.response.reservation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationPostPagingDto {
    boolean hasNext;
    List<ReservationPost> data;

    @Data
    @NoArgsConstructor
    public class ReservationPost {
        long id;
        String title;
        LocalDateTime date;
        AuthorDto author;
    }
}
