package com.umc7.ZIC.reservation.validation.validator;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.ReservationHandler;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.dto.PaymentRequestDTO;
import com.umc7.ZIC.reservation.service.ReservationQueryService;
import com.umc7.ZIC.reservation.validation.annotation.CheckReservationData;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ReservationDataCheckValidator implements ConstraintValidator<CheckReservationData, PaymentRequestDTO.KakaoPaymentCancelRequestDTO> {
    private final ReservationQueryService reservationQueryService;

    @Override
    public boolean isValid(PaymentRequestDTO.KakaoPaymentCancelRequestDTO request, ConstraintValidatorContext context) {
        boolean isValid = true;

        Reservation reservation = reservationQueryService.getReservationById(request.reservationId())
                .orElseThrow(() -> new ReservationHandler(ErrorStatus.RESERVATION_NOT_FOUND));

        context.disableDefaultConstraintViolation();
        if (reservation.getReservationDetail() == null) {
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_DETAIL_NOT_FOUND.getMessage()).addConstraintViolation();
            return false;
        }
        if (!Objects.equals(request.tid(), reservation.getReservationDetail().getTid())) {
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_TID_NOT_EQUAL.getMessage())
                    .addPropertyNode("tid").addConstraintViolation();
            isValid = false;
        }
        if (!Objects.equals(request.cancel_amount(), reservation.getReservationDetail().getAmount())) {
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_AMOUNT_NOT_EQUAL.getMessage())
                    .addPropertyNode("cancel_amount").addConstraintViolation();
            isValid = false;
        }
        if (!Objects.equals(request.cancel_tax_free_amount(), reservation.getReservationDetail().getTax_free_amount())) {
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_TAX_FREE_NOT_EQUAL.getMessage())
                    .addPropertyNode("cancel_tax_free_amount").addConstraintViolation();
            isValid = false;
        }
        if (!Objects.equals(request.cancel_vat_amount(), reservation.getReservationDetail().getVat_amount())) {
            context.buildConstraintViolationWithTemplate(ErrorStatus.RESERVATION_VAT_NOT_EQUAL.getMessage())
                    .addPropertyNode("cancel_vat_amount").addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
