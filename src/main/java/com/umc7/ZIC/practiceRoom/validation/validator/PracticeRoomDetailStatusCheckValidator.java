package com.umc7.ZIC.practiceRoom.validation.validator;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.practiceRoom.domain.enums.RoomStatus;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailResponseDto;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomDetailService;
import com.umc7.ZIC.practiceRoom.validation.annotation.CheckPracticeRoomDetailStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PracticeRoomDetailStatusCheckValidator implements ConstraintValidator<CheckPracticeRoomDetailStatus, Long> {
    private final PracticeRoomDetailService practiceRoomDetailService;

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext context) {
        try {
            PracticeRoomDetailResponseDto.GetDetailResponseDto practiceRoomDetail = practiceRoomDetailService.getPracticeRoomDetail(aLong);

            if (practiceRoomDetail.status() == RoomStatus.AVAILABLE) {
                return true;
            }

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.PRACTICEROOMDETAIL_NOT_AVAILABLE.getMessage()).addConstraintViolation();

            return false;
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND.getMessage()).addConstraintViolation();

            return false;
        }
    }
}
