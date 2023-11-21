package com.knu.matcher.dto.jobpost;

import com.knu.matcher.domain.jobpost.Comment;
import com.knu.matcher.domain.user.User;
import com.knu.matcher.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class CommentDto {
    private Long id;
    private String content;
    private LocalDateTime date;
    private UserInfoDto author;

    public static CommentDto fromDomain(Comment comment, User user){
        return CommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .date(comment.getDate())
                .author(UserInfoDto.fromDomain(user))
                .build();
    }
}
