package com.umc7.ZIC.reservation.validation.validator;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.reservation.service.ReservationQueryService;
import com.umc7.ZIC.reservation.validation.annotation.ExistReservation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationExistValidator implements ConstraintValidator<ExistReservation, Long> {
    private final ReservationQueryService reservationQueryService;

    @Override
    public void initialize(ExistReservation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext context) {
        boolean isValid = reservationQueryService.getReservationById(aLong).isPresent();

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_NOT_FOUND.getMessage()).addConstraintViolation();
        }

        return isValid;
    }
}
