package com.umc7.ZIC.security;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아 옴
        String token = jwtTokenProvider.resolveAccessToken();
        // log.info("헤더 토큰"+token);
        // 여기서 예외 발생시 securityconfig에서 설정한 다음 필터 체인인 ExceptionHandlerFilter으로 바로 넘어감.

        // 유효 토큰 확인
        if (token ==null){
            request.setAttribute("exception", ErrorStatus.JWT_NULL.getCode());
        } else if (jwtTokenProvider.validateToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

            filterChain.doFilter(request, response);
    }


}
