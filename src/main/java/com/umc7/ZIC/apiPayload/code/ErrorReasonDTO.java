package com.umc7.ZIC.apiPayload.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorReasonDTO(
        HttpStatus httpStatus,
        boolean isSuccess,
        String code,
        String message
) {
    public boolean getIsSuccess() {
        return isSuccess;
    }
}
