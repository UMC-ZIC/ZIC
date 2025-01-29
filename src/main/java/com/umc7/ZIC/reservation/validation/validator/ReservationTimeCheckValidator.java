package com.umc7.ZIC.reservation.validation.validator;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.reservation.dto.ReservationRequestDTO;
import com.umc7.ZIC.reservation.validation.annotation.CheckReservationTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReservationTimeCheckValidator implements ConstraintValidator<CheckReservationTime, ReservationRequestDTO.reservationRegistDTO> {

    @Override
    public void initialize(CheckReservationTime constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ReservationRequestDTO.reservationRegistDTO request, ConstraintValidatorContext context) {

        if(request.startTime().isAfter(request.endTime())){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_TIME_WRONG_REQUEST.getMessage())
                    .addPropertyNode("startTime").addConstraintViolation();
            return false;
        }

        return true;
    }
}
