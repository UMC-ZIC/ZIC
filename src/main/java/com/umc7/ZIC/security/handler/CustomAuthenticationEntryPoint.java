package com.umc7.ZIC.security.handler;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //security에서 설정한 권한이 필요한 api에서 인증이 되지 않은 유저가 접근시 작동.
        log.info("CustomAuthenticationEntryPoint");
        String exception = (String) request.getAttribute("exception");

        if (exception.equals(ErrorStatus.JWT_NULL.getCode())) {
            ExceptionFilter.responseError(response, ErrorStatus.JWT_NULL);
        }
    }
}
