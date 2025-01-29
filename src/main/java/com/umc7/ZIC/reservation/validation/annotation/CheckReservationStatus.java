package com.umc7.ZIC.reservation.validation.annotation;

import com.umc7.ZIC.reservation.domain.enums.Status;
import com.umc7.ZIC.reservation.validation.validator.ReservationStatusCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 해당 예약의 상태가 value를 통해 입력한 값과 같은지 검증
 * value - Status.PENDING 또는 Status.SUCCESS만 입력
 */
@Documented
@Constraint(validatedBy = ReservationStatusCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckReservationStatus {
    String message() default "해당 예약의 상태가 요청과 부합하지 않은 상태입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    // Status 값 받아오기
    Status value();
}
