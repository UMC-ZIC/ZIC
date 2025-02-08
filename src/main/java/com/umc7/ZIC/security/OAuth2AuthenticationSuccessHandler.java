package com.umc7.ZIC.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.domain.enums.RoleType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String ERROR_MESSAGE = "{\"error\":\"인증 도중 오류가 발생하였습니다.\"}";

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            User user = customOAuth2User.getUser();
            Long userId = user.getId();
            String role = user.getRole().name();
            String username = user.getName();

            // JWT 토큰 생성
            String jwtToken = jwtTokenProvider.createAccessToken(userId, role, username);

            // JSON 응답 작성
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(CHARACTER_ENCODING);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", jwtToken);
            responseBody.put("role", role);
            responseBody.put("username", username); // 유저 이름 추가
            responseBody.put("userId", userId); // 유저 ID Long 타입으로 추가

            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
            response.getWriter().flush();

            log.info("Authentication successful for user: {}", userId);
        } catch (Exception e) {
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding(CHARACTER_ENCODING);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(ERROR_MESSAGE);
            response.getWriter().flush();

            log.error("Authentication failed", e);
        }
    }
}