package com.knu.matcher.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditReservationPostDto {
    long reservationPostId;
    String title;
    String content;
}
