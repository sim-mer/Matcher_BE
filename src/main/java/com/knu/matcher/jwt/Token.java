package com.knu.matcher.jwt;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class Token {
    private String accessToken;
    private String refreshToken;
}
