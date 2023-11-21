package com.knu.matcher.dto.message;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
public class CreateMessageRequest {
    @Email(message = "이메일 형식으로 입력해주세요")
    private String receiverEmail;

    @NotEmpty(message = "내용을 입력해주세요")
    private String content;
}
