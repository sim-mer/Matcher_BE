package com.knu.matcher.domain.reservation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationPost {
    private long id;
    private String title;
    private String content;
    private LocalDateTime date;
    private String ownerEmail;
    private int rowSize;
    private int colSize;
}
