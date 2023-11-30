package com.knu.matcher.dto.jobpost;

import com.knu.matcher.domain.jobpost.JobPostSummaryWithUser;
import com.knu.matcher.dto.user.UserInfoDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class JobPostSummaryDto {
    private Long id;
    private String title;
    private LocalDateTime date;
    private List<String> image;
    private UserInfoDto author;

    public static JobPostSummaryDto fromDomain(JobPostSummaryWithUser jobPostSummaryWithUser) {
        UserInfoDto author = UserInfoDto.builder()
                .email(jobPostSummaryWithUser.getEmail())
                .name(jobPostSummaryWithUser.getName())
                .major(jobPostSummaryWithUser.getMajor())
                .stdNumber(jobPostSummaryWithUser.getStdNumber())
                .build();
        return JobPostSummaryDto.builder()
                .id(jobPostSummaryWithUser.getId())
                .title(jobPostSummaryWithUser.getTitle())
                .date(jobPostSummaryWithUser.getDate())
                .author(author)
                .build();
    }
}
