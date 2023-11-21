package com.knu.matcher.domain.jobpost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Comment {
    private Long id;
    private String content;
    private LocalDateTime date;
    private Long jobPostId;
    private String userEmail;
}
