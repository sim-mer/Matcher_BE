package com.knu.matcher.dto.message;

import lombok.Getter;

@Getter
public class CreateMessageRequest {
    private String receiverEmail;
    private String content;
}
