package com.umc7.ZIC.reservation.validation.validator;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.ReservationHandler;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.Status;
import com.umc7.ZIC.reservation.service.ReservationQueryService;
import com.umc7.ZIC.reservation.validation.annotation.CheckReservationStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationStatusCheckValidator implements ConstraintValidator<CheckReservationStatus, Long> {
    private final ReservationQueryService reservationQueryService;
    private Status statusFromAnnotation;

    @Override
    public void initialize(CheckReservationStatus constraintAnnotation) {
        this.statusFromAnnotation = constraintAnnotation.value(); // 전달받은 값을 초기화
    }

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext context) {
        Reservation reservation = reservationQueryService.getReservationById(aLong).orElseThrow(() -> new ReservationHandler(ErrorStatus.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() == statusFromAnnotation) {
            return true;
        }

        String customMessage = switch (statusFromAnnotation) {
            case PENDING -> ErrorStatus.RESERVATION_STATUS_NOT_PENDING.getMessage();
            case SUCCESS -> ErrorStatus.RESERVATION_STATUS_NOT_SUCCESS.getMessage();
            default -> "예약 상태가 올바르지 않습니다.";
        };

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(customMessage).addConstraintViolation();

        return false;
    }
}
