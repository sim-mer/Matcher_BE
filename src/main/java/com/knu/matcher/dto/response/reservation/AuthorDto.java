package com.knu.matcher.dto.response.reservation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {
    String email;
    String name;
    String major;
    String stdNumber;
}
