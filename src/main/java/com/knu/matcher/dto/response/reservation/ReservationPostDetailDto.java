package com.knu.matcher.dto.response.reservation;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReservationPostDetailDto {
    long id;
    String title;
    LocalDateTime date;
    AuthorDto author;
    int rowSize;
    int colSize;
    List<Seat> seatList;

    @Data
    @Builder
    public static class Seat {
        int rowNumber;
        int colNumber;
        String booker;
    }
}
