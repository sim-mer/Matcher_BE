package com.knu.matcher.controller;

import com.knu.matcher.dto.request.CreateMessageDto;
import com.knu.matcher.dto.response.message.MessageDetailDto;
import com.knu.matcher.dto.response.message.MessageListDto;
import com.knu.matcher.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private MessageService messageService;

    @PostMapping
    public long createMessage(@RequestBody CreateMessageDto dto) {
        return messageService.createMessage(dto);
    }

    @GetMapping("/{userEmail}")
    public MessageDetailDto getMessage(@PathVariable String userEmail) {
        return messageService.getMessage(userEmail);
    }

    @GetMapping
    public MessageListDto getMessages() {
        return messageService.getMessages();
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable long id) {
        messageService.deleteMessage(id);
    }

}
