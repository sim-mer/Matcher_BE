package com.knu.matcher.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;
    private String major;
    private String stdNumber;
}
