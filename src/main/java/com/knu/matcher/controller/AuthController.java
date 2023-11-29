package com.knu.matcher.controller;

import com.knu.matcher.dto.user.SignUpRequestDto;
import com.knu.matcher.jwt.JwtUtil;
import com.knu.matcher.jwt.Token;
import com.knu.matcher.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @GetMapping("/login")
    public Token login(@RequestParam String id, @RequestParam String pw){
        if(!userService.checkUser(id, pw))
            throw new RuntimeException("유효하지 않은 사용자입니다.");
        return jwtUtil.createToken(id);
    }

    @GetMapping("/reissue")
    public String reissueAccessToken(@RequestHeader("Authorization") String refreshToken){
        return jwtUtil.reissueAccessToken(refreshToken);
    }

    @PostMapping("/signup")
    public Token signup(@RequestBody SignUpRequestDto dto){
        userService.createUser(dto);
        return jwtUtil.createToken(dto.getEmail());
    }


}
