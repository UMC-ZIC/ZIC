package com.umc7.ZIC.reservation.dto;

import com.umc7.ZIC.reservation.domain.enums.ReservationStatus;
import com.umc7.ZIC.reservation.validation.annotation.CheckReservationData;
import com.umc7.ZIC.reservation.validation.annotation.CheckReservationStatus;
import com.umc7.ZIC.reservation.validation.annotation.ExistReservation;
import com.umc7.ZIC.reservation.validation.validationSequence.ValidationStep;
import jakarta.validation.constraints.*;
import lombok.Builder;

public class PaymentRequestDTO {

    /**
     * KAKAO PAY 결제 승인을 오청하기 위한 클라이언트로 부터 받을 DTO
     * @param reservationId
     * @param tid
     * @param partner_order_id
     * @param pg_token
     */
    @Builder
    public record KakaoPaymentApproveRequestDTO(
            @NotNull(message = "예약 ID는 필수 입력 항목입니다.")
            @ExistReservation(groups = ValidationStep.ExistValidation.class)
            @CheckReservationStatus(value = ReservationStatus.PENDING, groups = ValidationStep.StatusValidation.class)
            Long reservationId,

            @NotBlank(message = "거래 ID(TID)는 필수 입력 항목입니다.")
            String tid,

            @NotBlank(message = "예약 번호는 필수 입력 항목입니다.")
            String partner_order_id,

            @NotBlank(message = "PG 토큰은 필수 입력 항목입니다.")
            String pg_token
    ) {}
    
    /**
     * Kakao Pay 결제 취소 요청하기 위한 클라이언트로 부터 받을 DTO
     * @param reservationId DB에 저장된 예약 id
     * @param tid Kakao Pay 결제 고유 번호
     * @param cancel_amount 취소 금액
     * @param cancel_tax_free_amount 취소 비과세 금액
     * @param cancel_vat_amount 취소 부과세 금액
     */
    @Builder
    @CheckReservationData(groups = ValidationStep.dataValidation.class)
    public record KakaoPaymentCancelRequestDTO(
            @NotNull(message = "예약 ID는 필수 입력 항목입니다.")
            @ExistReservation(groups = ValidationStep.ExistValidation.class)
            @CheckReservationStatus(value = ReservationStatus.SUCCESS, groups = ValidationStep.StatusValidation.class)
            Long reservationId,

            @NotBlank(message = "거래 ID(TID)는 필수 입력 항목입니다.")
            String tid,

            @NotNull(message = "취소 금액은 필수 입력 항목입니다.")
            @Positive(message = "취소 금액은 0보다 커야 합니다.")
            Integer cancel_amount,

            @NotNull(message = "취소 비과세 금액은 필수 입력 항목입니다.")
            @PositiveOrZero(message = "취소 비과세 금액은 0 이상이어야 합니다.")
            Integer cancel_tax_free_amount,

            @NotNull(message = "취소 부가세 금액은 필수 입력 항목입니다.")
            @PositiveOrZero(message = "취소 부가세 금액은 0 이상이어야 합니다.")
            Integer cancel_vat_amount

            //Integer cancel_available_amount
    ) {}
}
