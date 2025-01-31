package com.umc7.ZIC.reservation.validation.annotation;

import com.umc7.ZIC.reservation.validation.validator.ReservationTimeOverlapCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Request에서 보낸 시간과 겹치는 예약이 존재하는지 검증
 */
@Documented
@Constraint(validatedBy = ReservationTimeOverlapCheckValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckReservationTimeOverlap {
    String message() default "선택하신 시간에 이미 예약이 있습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
