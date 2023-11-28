package com.knu.matcher.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReserveSeatDto {
    int rowNumber;
    int colNumber;
}
