package com.knu.matcher.domain.jobpost;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class JobPostSummaryWithUser {
    private Long id;
    private String title;
    private LocalDateTime date;

    private String email;
    private String name;
    private String major;
    private String stdNumber;
}
