package com.knu.matcher.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 1주
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;  // 2주

    public Token createToken(String id) {
        long current = System.currentTimeMillis();
        Date accessExpireTime = new Date(current + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshExpireTime = new Date(current + REFRESH_TOKEN_EXPIRE_TIME);
        String accessToken = generateToken(id, accessExpireTime);
        String refreshToken = generateToken(id, refreshExpireTime);
        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String reissueAccessToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            String email = extractEmail(refreshToken);
            Date expireTime = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME);
            return generateToken(email, expireTime);
        } else {
            throw new JwtException("리프레시 토큰이 유효하지 않습니다.");
        }
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private String generateToken(String email, Date expireTime) {
        Key secretKey = createSecretKey(secret);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expireTime)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(createSecretKey(secret)).parseClaimsJws(token).getBody();
    }

    public Key createSecretKey(String secret) {
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 32) {
            try {
                keyBytes = Arrays.copyOf(MessageDigest.getInstance("SHA-256").digest(keyBytes), 32);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
