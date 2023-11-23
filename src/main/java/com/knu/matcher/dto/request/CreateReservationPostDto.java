package com.knu.matcher.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateReservationPostDto {
    String title;
    String content;
    int rowSize;
    int colSize;
    List<Seat> disableSeatList;

    @Data
    @NoArgsConstructor
    public static class Seat {
        int rowNumber;
        int colNumber;
    }
}
