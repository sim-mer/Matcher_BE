package com.knu.matcher.controller;

import com.knu.matcher.annotation.TokenUserEmail;
import com.knu.matcher.dto.message.CreateMessageRequest;
import com.knu.matcher.dto.message.MessageDetailDto;
import com.knu.matcher.dto.message.MessageSummaryDto;
import com.knu.matcher.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public Long createMessage(@RequestBody @Valid CreateMessageRequest dto,@TokenUserEmail String userEmail) {
        return messageService.createMessage(userEmail, dto);
    }


    @GetMapping
    public List<MessageSummaryDto> getMessagesSummary(@TokenUserEmail String userEmail) {
        return messageService.getMessagesSummary(userEmail);
    }

    @GetMapping("/{otherUserEmail}")
    public List<MessageDetailDto> getMessagesDetail(@PathVariable String otherUserEmail, @TokenUserEmail String userEmail) {
        return messageService.getMessagesDetail(userEmail, otherUserEmail);
    }

    @DeleteMapping("/{mid}")
    public void deleteMessage(@PathVariable long mid, @TokenUserEmail String userEmail) {
        messageService.deleteMessage(userEmail, mid);
    }

}
