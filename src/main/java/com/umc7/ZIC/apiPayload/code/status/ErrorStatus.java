package com.umc7.ZIC.apiPayload.code.status;

import com.umc7.ZIC.apiPayload.code.BaseErrorCode;
import com.umc7.ZIC.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // 지역 관련 에러
    REGION_NOT_FOUND(HttpStatus.BAD_REQUEST, "REGION4001", "해당 지역이 없습니다."),

    // 사용자 관려 에러
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "사용자가 없습니다."),
    
    //악기 관련 에러
    INSTRUMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "INSTRUMENT4001", "해당 악기가 없습니다."),

    //연습실 내부 연습방 관련에러
    PRACTICEROOMDETAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "PRACTICEROOMDETAIL4004", "연습실 내부에 해당 연습방이 없습니다."),
    PRACTICEROOMDETAIL_NOT_CREATE(HttpStatus.BAD_GATEWAY, "PRACTICEROOMDETAIL4000", "연습실 생성에 실패하였습니다.."),

    //연습실 관련 에러
    PRACTICEROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "PRACTICEROOM4004", "연습실이 없습니다."),
    PRACTICEROOM_NOT_OWNER_ROLE(HttpStatus.NOT_FOUND, "PRACTICEROOM4002", "대여자 권한이 필요합니다."),
    PRACTICEROOM_AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED, "JPRACTICEROOM4001", "해당 연습실에 권한이 없습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
