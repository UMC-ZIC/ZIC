package com.umc7.ZIC.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class ExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // jwtfilter 에서 예외가 발생해 이 곳으로 이동 후 처리.
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException");
            responseError(response, ErrorStatus.JWT_EXPIRED);
        } catch (MalformedJwtException e) {
            log.info("MalformedJwtException");
            responseError(response, ErrorStatus.JWT_MALFORMED);
        } catch (UnsupportedJwtException e) {
            log.info("UnsupportedJwtException");
            responseError(response, ErrorStatus.JWT_UNSUPPORTED);
        } catch (SignatureException e) {
            log.info("SignatureException");
            responseError(response, ErrorStatus.JWT_Signature_FAILED);
        } catch (IllegalArgumentException e){
            log.info("IllegalArgumentException");
            responseError(response, ErrorStatus.JWT_NULL);
        }

    }

    public static void responseError(HttpServletResponse response, ErrorStatus errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        ObjectMapper objectMapper = new ObjectMapper();

        ApiResponse<String> failureResponse = ApiResponse.onFailure(errorCode.getCode(),errorCode.getMessage(),null);
        String s = objectMapper.writeValueAsString(failureResponse);

        response.getWriter().write(s);
        ApiResponse.onFailure(errorCode.getCode(),errorCode.getMessage(),null);
    }
}
