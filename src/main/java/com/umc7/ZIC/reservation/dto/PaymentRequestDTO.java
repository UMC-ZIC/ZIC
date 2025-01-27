package com.umc7.ZIC.reservation.dto;

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
            Long reservationId,
            String tid,
            String partner_order_id,
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
    public record KakaoPaymentCancelRequestDTO(
            Long reservationId,
            String tid,
            Integer cancel_amount,
            Integer cancel_tax_free_amount,
            Integer cancel_vat_amount
            //Integer cancel_available_amount
    ) {}
}
