package com.knu.matcher.dto.jobpost;

import com.knu.matcher.dto.user.UserInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class JobPostDetailResponse {
    private Long id;
    private String title;
    private LocalDateTime date;
    private UserInfoDto author;

    private String content;
    private List<CommentDto> commentList;
}
