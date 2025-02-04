package com.umc7.ZIC.practiceRoom.validation.validator;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomDetailService;
import com.umc7.ZIC.practiceRoom.validation.annotation.ExistPracticeRoomDetail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PracticeRoomDetailExistValidator implements ConstraintValidator<ExistPracticeRoomDetail, Long> {
    private final PracticeRoomDetailService practiceRoomDetailService;

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext context) {
        try {
            practiceRoomDetailService.getPracticeRoomDetail(aLong);
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND.getMessage()).addConstraintViolation();
            return false;
        }

        return true;
    }
}
