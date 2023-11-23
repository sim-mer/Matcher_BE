package com.knu.matcher.dto.response.reservation;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationPostPagingDto {
    long id;
    String title;
    LocalDateTime date;
    String ownerName;
}
