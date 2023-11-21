package com.knu.matcher.domain.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Message {
    private Long id;
    private String content;
    private LocalDateTime date;
    private String senderEmail;
    private String receiverEmail;
}
