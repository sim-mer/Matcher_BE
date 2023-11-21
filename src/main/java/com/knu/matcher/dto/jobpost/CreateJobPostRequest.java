package com.knu.matcher.dto.jobpost;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class CreateJobPostRequest {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
}
