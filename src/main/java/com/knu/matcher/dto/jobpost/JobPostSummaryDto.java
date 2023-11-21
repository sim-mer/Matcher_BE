package com.knu.matcher.dto.jobpost;

import com.knu.matcher.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class JobPostSummaryDto {
    private Long id;
    private String title;
    private LocalDateTime date;
    private UserInfoDto author;
}
