package com.knu.matcher.dto.response.reservation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ReservationPostDetailDto {
    long id;
    String title;
    LocalDateTime date;
    AuthorDto author;
    int rowSize;
    int colSize;
    List<Seat> seatList;

    @Data
    @NoArgsConstructor
    public class Seat {
        int rowNumber;
        int colNumber;
        String booker;
    }
}
