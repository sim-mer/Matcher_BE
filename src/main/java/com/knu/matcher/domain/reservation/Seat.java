package com.knu.matcher.domain.reservation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Seat {
    private long id;
    private int rowNumber;
    private int colNumber;
    private long reservationPostId;
}
