package com.knu.matcher.web;

import com.knu.matcher.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenCheckFilter implements Filter {
    private static final String[] whiteList = {"/login", "/reissue", "/signup","/image/**"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();

        HttpServletResponse res =(HttpServletResponse) response;

        JwtUtil jwtUtil = new JwtUtil();

        if (isCheck(requestURI)) {
            String token = req.getHeader("Authorization");
            if (token == null) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                res.getWriter().write("{\"status\":\"JwtError\", \"message\":\"토큰이 존재하지 않습니다.\"}");
                return;
            }
            if(jwtUtil.validateToken(token)){
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");
                res.getWriter().write("{\"status\":\"JwtError\", \"message\":\"토큰이 유효하지 않습니다.\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isCheck(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }

}
