package com.umc7.ZIC.practiceRoom.validation.annotation;

import com.umc7.ZIC.practiceRoom.validation.validator.PracticeRoomDetailStatusCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 해당 연습실 방이 이용 가능한 상태인지 검증
 */
@Documented
@Constraint(validatedBy = PracticeRoomDetailStatusCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPracticeRoomDetailStatus {
    String message() default "연습실이 현재 이용 가능한 상태가 아닙니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
