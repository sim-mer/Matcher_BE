package com.knu.matcher.service;

import com.knu.matcher.domain.user.User;
import com.knu.matcher.dto.user.EditUserInfoRequest;
import com.knu.matcher.dto.user.UserInfoDto;
import com.knu.matcher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean checkUser(String id, String pw){
        User user = userRepository.findByEmail(id);
        if(user == null)
            return false;
        return user.getPassword().equals(pw);
    }

    public UserInfoDto getUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if(user == null){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        return UserInfoDto.fromDomain(user);
    }

    public void updateUser(EditUserInfoRequest dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if(user == null){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        User editUser = User.builder().email(userEmail).name(dto.getName()).major(dto.getMajor()).stdNumber(dto.getStdNumber()).build();
        userRepository.updateUser(editUser);
    }

    public void deleteUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if(user == null){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        userRepository.deleteUser(userEmail);
    }
}
