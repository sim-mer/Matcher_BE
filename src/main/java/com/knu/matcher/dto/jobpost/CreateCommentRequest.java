package com.knu.matcher.dto.jobpost;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class CreateCommentRequest {
    @NotNull(message = "게시글 id를 입력해주세요.")
    private Long jobPostId;
    @NotNull(message = "내용을 입력해주세요.")
    private String content;
}
