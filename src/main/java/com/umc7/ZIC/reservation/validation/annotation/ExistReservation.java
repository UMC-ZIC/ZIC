package com.umc7.ZIC.reservation.validation.annotation;

import com.umc7.ZIC.reservation.validation.validator.ReservationExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 해당 예약이 존재하는지 검증
 */
@Documented
@Constraint(validatedBy = ReservationExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistReservation {
    String message() default "해당 예약 데이터가 없습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
