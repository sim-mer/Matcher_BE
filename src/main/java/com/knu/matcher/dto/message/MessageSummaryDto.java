package com.knu.matcher.dto.message;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageSummaryDto {
    private final String userEmail;
    private final String userName;
}
