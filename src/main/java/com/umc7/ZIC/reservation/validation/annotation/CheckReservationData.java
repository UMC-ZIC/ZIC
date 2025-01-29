package com.umc7.ZIC.reservation.validation.annotation;

import com.umc7.ZIC.reservation.validation.validator.ReservationDataCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 결제 취소하기 위해 입력한 데이터가 DB에 저장된 예약 데이터와 같은지 검증
 */
@Documented
@Constraint(validatedBy = ReservationDataCheckValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckReservationData {
    String message() default "요청으로 입력한 정보와 저장된 예약 데이터가 일치하지 않습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
