package com.knu.matcher.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ReserveSeatsRequest {
    private List<ReserveSeatDto> seatList;
}
