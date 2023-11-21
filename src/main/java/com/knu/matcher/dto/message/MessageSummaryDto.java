package com.knu.matcher.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MessageSummaryDto {
    private final String userEmail;
    private final String userName;
}
