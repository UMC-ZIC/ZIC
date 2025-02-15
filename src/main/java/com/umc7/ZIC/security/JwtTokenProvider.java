package com.umc7.ZIC.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String stringSecretKey;  // String 형식보다는 Key 형식이 안전

    private Key secretKey;

    // 토큰의 유효시간
    public static final long TOKEN_VALID_TIME = 1000L * 60 * 60 * 24;    // 엑세스 토큰 24시간
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 7;   // 일주일

    private final CustomUserDetailsService userDetailService;

    // 객체 초기화, secretKey 를 Base64 로 인코딩 후 Key 객체로 변환
    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getDecoder().decode(stringSecretKey.getBytes(StandardCharsets.UTF_8));
        secretKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }


    // Jwt AccessToken 생성
    public String createAccessToken(Long userId, String userType, String username) {
        return Jwts.builder()
                .setHeaderParam("type", "accessToken")
                .claim("userId", userId)
                .claim("userType", userType)
                .claim("userName", username)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Access 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALID_TIME)) // Access 토큰 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Jwt RefreshToken 생성
    public String createRefreshToken(Long userId) {
        return Jwts.builder()
                .setHeaderParam("type", "refreshToken")
                .claim("userId", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Refresh 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME)) // Refresh 토큰 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
        return true;
    }

    // Request 의 Header 에서 token 값을 가져옵니다. "Authorization" : "TOKEN 값'
    public String resolveAccessToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    // UserId 추출
    public Long getUserIdFromToken() {
        String accessToken = resolveAccessToken();
        return getUserIdInToken(accessToken);
    }

    // UserType 추출
    public String getUserTypeFromToken() {
        String accessToken = resolveAccessToken();
        return getUserTypeInToken(accessToken);
    }

    // 토큰에서 userId 추출
    public Long getUserIdInToken(String token) {
        return extractAllClaims(token).get("userId", Long.class);
    }

    // 토큰에서 userType 추출
    public String getUserTypeInToken(String token) {
        return extractAllClaims(token).get("userType", String.class);
    }

    // 토큰 parsing
    public Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    // JWT 토큰에서 역할 권한 조회
    public Authentication getAuthentication(String token) {
        Long userId = getUserIdInToken(token);

        UserDetails userDetails = userDetailService.loadUserByUsername(String.valueOf(userId));

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}


