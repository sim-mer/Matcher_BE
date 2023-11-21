package com.knu.matcher.dto.jobpost;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class EditJobPostRequest {
    @NotNull(message = "게시글 id를 입력해주세요.")
    private Long id;
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
}
