package com.knu.matcher.domain.jobpost;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class JobPost {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime date;
    private String userEmail;
}
