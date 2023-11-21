package com.knu.matcher.controller;

import com.knu.matcher.dto.message.CreateMessageRequest;
import com.knu.matcher.dto.message.MessageDetailDto;
import com.knu.matcher.dto.message.MessageSummaryDto;
import com.knu.matcher.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public Long createMessage(@RequestBody CreateMessageRequest dto) {
        String userEmail = "user2@example.com";
        return messageService.createMessage(userEmail, dto);
    }


    @GetMapping
    public List<MessageSummaryDto> getMessagesSummary() {
        String userEmail = "user2@example.com";

        return messageService.getMessagesSummary(userEmail);
    }

    @GetMapping("/{otherUserEmail}")
    public List<MessageDetailDto> getMessagesDetail(@PathVariable String otherUserEmail) {
        String userEmail = "user2@example.com";
        return messageService.getMessagesDetail(userEmail, otherUserEmail);
    }

    @DeleteMapping("/{mid}")
    public void deleteMessage(@PathVariable long mid) {
        String userEmail = "user2@example.com";
        messageService.deleteMessage(userEmail, mid);
    }

}
