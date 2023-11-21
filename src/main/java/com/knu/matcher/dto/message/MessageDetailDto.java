package com.knu.matcher.dto.message;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageDetailDto {
    private final Long id;
    private final String senderEmail;
    private final String receiverEmail;
    private final String content;
    private final LocalDateTime date;
}
