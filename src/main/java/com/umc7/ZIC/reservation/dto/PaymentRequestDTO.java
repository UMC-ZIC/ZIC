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
}
