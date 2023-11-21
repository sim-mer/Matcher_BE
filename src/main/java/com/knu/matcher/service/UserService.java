package com.knu.matcher.service;

import com.knu.matcher.domain.user.User;
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
}
