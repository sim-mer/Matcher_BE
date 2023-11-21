package com.knu.matcher.domain.jobpost;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CommentWithUser {

    private Long id;
    private String content;
    private LocalDateTime date;
    private Long jobPostId;
    private String userEmail;
    private String name;
    private String major;
    private String stdNumber;
}
