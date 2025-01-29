package com.umc7.ZIC.practiceRoom.validation.validator;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomDetailRepository;
import com.umc7.ZIC.practiceRoom.validation.annotation.ExistPracticeRoomDetail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PracticeRoomDetailExistValidator implements ConstraintValidator<ExistPracticeRoomDetail, Long> {
    // TODO : Repository를 Service로 변경하기
    private final PracticeRoomDetailRepository practiceRoomDetailRepository;

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext context) {
        Optional<PracticeRoomDetail> practiceRoomDetail = practiceRoomDetailRepository.findById(aLong);

        if (practiceRoomDetail.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND.getMessage()).addConstraintViolation();

            return false;
        }

        return true;
    }
}
