package com.knu.matcher.dto.response.reservation;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorDto {
    String email;
    String name;
    String major;
    String stdNumber;
}
