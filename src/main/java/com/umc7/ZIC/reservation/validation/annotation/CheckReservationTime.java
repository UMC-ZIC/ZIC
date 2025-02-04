package com.umc7.ZIC.reservation.validation.annotation;

import com.umc7.ZIC.reservation.validation.validator.ReservationTimeCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Request의 startTime이 endTime보다 빠른지 검증
 */
@Documented
@Constraint(validatedBy = ReservationTimeCheckValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckReservationTime {
    String message() default "startTime이 endTime보다 늦어선 안됩니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
