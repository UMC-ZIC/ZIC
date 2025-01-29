package com.umc7.ZIC.practiceRoom.validation.validator;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomDetailHandler;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.domain.enums.RoomStatus;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomDetailRepository;
import com.umc7.ZIC.practiceRoom.validation.annotation.CheckPracticeRoomDetailStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PracticeRoomDetailStatusCheckValidator implements ConstraintValidator<CheckPracticeRoomDetailStatus, Long> {
    // TODO : Repository를 Service로 변경하기
    private final PracticeRoomDetailRepository practiceRoomDetailRepository;

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext context) {
        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findById(aLong)
                .orElseThrow(() -> new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND));

        if (practiceRoomDetail.getStatus() == RoomStatus.AVAILABLE) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(ErrorStatus.PRACTICEROOMDETAIL_NOT_AVAILABLE.getMessage()).addConstraintViolation();
        return false;
    }
}
