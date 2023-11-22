package com.knu.matcher.dto.user;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Getter
public class EditUserInfoRequest {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    @NotEmpty(message = "전공을 입력해주세요.")
    private String major;
    @NotEmpty(message = "학번을 입력해주세요.")
    @Length(min=2,max=2, message = "학번은 2자리입니다.")
    @Positive(message = "학번은 양수입니다.")
    private String stdNumber;
}
