package com.knu.matcher.dto.message;

import com.knu.matcher.domain.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class MessageDetailDto {
    private final Long id;
    private final String senderEmail;
    private final String receiverEmail;
    private final String content;
    private final LocalDateTime date;

    public static MessageDetailDto fromDomain(Message message){
        return MessageDetailDto.builder()
                .id(message.getId())
                .senderEmail(message.getSenderEmail())
                .receiverEmail(message.getReceiverEmail())
                .content(message.getContent())
                .date(message.getDate())
                .build();
    }
}
