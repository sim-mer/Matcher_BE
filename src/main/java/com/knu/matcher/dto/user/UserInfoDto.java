package com.knu.matcher.dto.user;

import com.knu.matcher.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserInfoDto {
    private String email;
    private String name;
    private String major;
    private String stdNumber;

    public static UserInfoDto fromDomain(User user){
        return UserInfoDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .major(user.getMajor())
                .stdNumber(user.getStdNumber())
                .build();
    }
}
