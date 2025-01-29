package com.umc7.ZIC.practiceRoom.validation.annotation;

import com.umc7.ZIC.practiceRoom.validation.validator.PracticeRoomDetailExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 해당 연습실 방이 존재하는지 검증
 */
@Documented
@Constraint(validatedBy = PracticeRoomDetailExistValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistPracticeRoomDetail {
    String message() default "해당 예약 데이터가 없습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
