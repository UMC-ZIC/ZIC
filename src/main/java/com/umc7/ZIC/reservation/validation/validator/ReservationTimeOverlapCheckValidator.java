package com.umc7.ZIC.reservation.validation.validator;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.ReservationStatus;
import com.umc7.ZIC.reservation.dto.ReservationRequestDTO;
import com.umc7.ZIC.reservation.service.ReservationQueryService;
import com.umc7.ZIC.reservation.validation.annotation.CheckReservationTimeOverlap;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservationTimeOverlapCheckValidator implements ConstraintValidator<CheckReservationTimeOverlap, ReservationRequestDTO.reservationRegistDTO> {
    private final ReservationQueryService reservationQueryService;

    @Override
    public boolean isValid(ReservationRequestDTO.reservationRegistDTO request, ConstraintValidatorContext context) {
        Optional<List<Reservation>> reservationSuccessList = reservationQueryService.overlappingReservation(request.practiceRoomDetail(), request.date(), request.startTime(), request.endTime(), ReservationStatus.SUCCESS);
        Optional<List<Reservation>> reservationPendingList = reservationQueryService.overlappingReservation(request.practiceRoomDetail(), request.date(), request.startTime(), request.endTime(), ReservationStatus.PENDING);

        if (reservationSuccessList.isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_TIME_OVERLAP_SECCESS_REQUEST.getMessage())
                    .addPropertyNode("startTime").addConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_TIME_OVERLAP_SECCESS_REQUEST.getMessage())
                    .addPropertyNode("endTime").addConstraintViolation();

            return false;
        } else if  (reservationPendingList.isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_TIME_OVERLAP_PENDING_REQUEST.getMessage())
                    .addPropertyNode("startTime").addConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_TIME_OVERLAP_PENDING_REQUEST.getMessage())
                    .addPropertyNode("endTime").addConstraintViolation();

            return false;
        }

        return true;
    }
}
