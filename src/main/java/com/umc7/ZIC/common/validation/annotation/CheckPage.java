package com.umc7.ZIC.common.validation.annotation;

import com.umc7.ZIC.common.validation.validator.PageCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Query variable의 page가 1이상인지 검증
 */
@Documented
@Constraint(validatedBy = PageCheckValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPage {
    String message() default "page는 0보다 커야합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
