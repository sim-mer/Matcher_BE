package com.knu.matcher.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpRequestDto {
    private String email;
    private String password;
    private String name;
    private String major;
    private String stdNumber;
}
