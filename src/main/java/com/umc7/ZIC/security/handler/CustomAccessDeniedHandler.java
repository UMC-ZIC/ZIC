package com.umc7.ZIC.security.handler;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //인증된 사용자가 권한이 없는 리소스에 접근시 호출 ex) 권한 부족
        log.error("CustomAccessDeniedHandler");

        ExceptionFilter.responseError(response, ErrorStatus.JWT_AUTHORIZATION_FAILED); //권한이 없음.
    }
}
