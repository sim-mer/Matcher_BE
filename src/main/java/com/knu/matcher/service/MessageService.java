package com.knu.matcher.service;

import com.knu.matcher.domain.message.Message;
import com.knu.matcher.domain.user.User;
import com.knu.matcher.dto.message.CreateMessageRequest;
import com.knu.matcher.dto.message.MessageDetailDto;
import com.knu.matcher.dto.message.MessageSummaryDto;
import com.knu.matcher.repository.MessageRepository;
import com.knu.matcher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;


    public Long createMessage(String userEmail, CreateMessageRequest dto) {
        Long id = messageRepository.findLastMessageId() + 1;
        Message message = Message.builder()
                .id(id)
                .content(dto.getContent())
                .date(LocalDateTime.now())
                .senderEmail(userEmail)
                .receiverEmail(dto.getReceiverEmail())
                .build();
        Long mid = messageRepository.save(message);
        if(mid == null){
            throw new IllegalStateException("메시지 전송에 실패하였습니다.");
        }
        return mid;
    }
    public List<MessageSummaryDto> getMessagesSummary(String userEmail) {
        List<String> emails = messageRepository.findUsersEmailInChatByUserEmail(userEmail);
        return emails.stream().map(email ->{
            User user = userRepository.findByEmail(email);
            String userName = user.getName();
            MessageSummaryDto messageSummaryDto = MessageSummaryDto.builder()
                    .userEmail(email)
                    .userName(userName)
                    .build();
            return messageSummaryDto;
        }).collect(Collectors.toList());
    }

    public List<MessageDetailDto> getMessagesDetail(String userEmail, String otherUserEmail) {
        List<Message> messageList = messageRepository.findMessagesByEmail(userEmail,otherUserEmail);

        return messageList.stream().map(MessageDetailDto::fromDomain).collect(Collectors.toList());
    }


    public void deleteMessage(String userEmail, long mid) {
        Message message = messageRepository.findById(mid);
        if(message == null){
            throw new IllegalArgumentException("메시지가 존재하지 않습니다.");
        }
        if(!message.getSenderEmail().equals(userEmail)){
            throw new IllegalArgumentException("해당 메시지를 삭제할 권한이 없습니다.");
        }
        messageRepository.delete(mid);
    }
}
