package com.knu.matcher.dto.jobpost;

import com.knu.matcher.domain.jobpost.Comment;
import com.knu.matcher.domain.jobpost.CommentWithUser;
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

    public static CommentDto fromDomain(CommentWithUser commentWithUser){
        UserInfoDto author = UserInfoDto.builder()
                .name(commentWithUser.getName())
                .email(commentWithUser.getUserEmail())
                .major(commentWithUser.getMajor())
                .stdNumber(commentWithUser.getStdNumber())
                .build();
        return CommentDto.builder()
                .id(commentWithUser.getId())
                .content(commentWithUser.getContent())
                .date(commentWithUser.getDate())
                .author(author)
                .build();
    }
}
