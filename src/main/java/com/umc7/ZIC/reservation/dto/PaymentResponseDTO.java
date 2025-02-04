package com.umc7.ZIC.reservation.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public class PaymentResponseDTO {

    /**
     * KAKAO PAY 결제 요청 응답 DTO
     * @param tid KAKAO PAY 결제 고유 번호
     * @param next_redirect_mobile_url 모바일 KAKAO PAY 인증 리다이렉트 URL
     * @param next_redirect_pc_url PC KAKAO PAY 인증 리다이렉트 URL
     * @param next_redirect_app_url 모바일로 보는 PC 웹사이트 KAKAO PAY 인증 리다이렉트 URL
     * @param created_at
     */
    @Builder
    public record KakaoPaymentReadyResponseDTO(
            String tid,
            String next_redirect_mobile_url,
            String next_redirect_pc_url,
            String next_redirect_app_url,
            String created_at
    ) {}

    /**
     * KAKAO 결제 승인 응답 DTO
     * @param cid 가맹점 고유 번호
     * @param aid
     * @param tid
     * @param partner_user_id 회원 정보 : 회원 이름
     * @param partner_order_id 클라이언트에서 받은 고유 주문 번호
     * @param payment_method_type 지불 방식 : CARD or MONEY
     * @param item_name 상품 이름
     * @param quantity 상품의 수량 : 여기선 예약한 시간
     * @param amount 총 가격
     * @param card_info 카드 정보
     * @param created_at
     * @param approved_at
     */
    @Builder
    public record KakaoPaymentApproveResponseDTO(
            String cid,
            String aid,
            String tid,
            String partner_user_id,
            String partner_order_id,
            String payment_method_type,
            String item_name,
            int quantity,
            Amount amount,
            CardInfo card_info,
            LocalDateTime created_at,
            LocalDateTime approved_at
    ) {
        // 상품 결제 가격 정보
        public record Amount(
                int total,
                int tax_free,
                int vat,
                int discount,
                int point,
                int green_deposit
        ) {}

        // 카드 결제 시 카드 정보
        public record CardInfo(
                String interest_free_install,
                String bin,
                String card_type,
                String card_mid,
                String approved_id,
                String install_month,
                String installment_type,
                String kakaopay_purchase_corp,
                String kakaopay_purchase_corp_code,
                String kakaopay_issuer_corp,
                String kakaopay_issuer_corp_code
        ) {}
    }

    /**
     * 클라이언트에게 전송할 예약 상세 id + KAKAO PAY 결제 승인 응답 DTO
     * @param reservation_detail_id 예약 상세 id
     * @param PaymentResult KAKAO PAY 결제 승인 응답 DTO
     * @param <T> PaymentResult를 받을 DTO 종류
     */
    @Builder
    public record KakaoPaymentResultDTO<T>(
            Long reservation_detail_id,
            T PaymentResult
    ) {}

    /**
     * KAKAO 결제 취소 응답 DTO
     * @param aid
     * @param cid
     * @param tid
     * @param status
     * @param partner_user_id
     * @param partner_order_id
     * @param payment_method_type
     * @param item_name
     * @param quantity
     * @param amount
     * @param approved_cancel_amount
     * @param canceled_amount
     * @param cancel_available_amount
     * @param created_at
     * @param approved_at
     * @param canceled_at
     */
    @Builder
    public record KakaoPaymentCancelResponseDTO(
            String aid,
            String cid,
            String tid,
            String status,
            String partner_user_id,
            String partner_order_id,
            String payment_method_type,
            String item_name,
            int quantity,
            Amount amount,
            ApprovedCancelAmount approved_cancel_amount,
            CanceledAmount canceled_amount,
            CancelAvailableAmount cancel_available_amount,
            LocalDateTime created_at,
            LocalDateTime approved_at,
            LocalDateTime canceled_at
    ) {
        // 상품 결제 가격 정보
        public record Amount(
                int total,
                int tax_free,
                int vat,
                int discount,
                int point,
                int green_deposit
        ) {}

        // 이번 요청으로 취소된 금액
        public record ApprovedCancelAmount(
                int total,
                int tax_free,
                int vat,
                int discount,
                int point,
                int green_deposit
        ) {}

        // 누계 취소 금액
        public record CanceledAmount(
                int total,
                int tax_free,
                int vat,
                int discount,
                int point,
                int green_deposit
        ) {}

        // 남은 취소 가능 금액
        public record CancelAvailableAmount(
                int total,
                int tax_free,
                int vat,
                int discount,
                int point,
                int green_deposit
        ) {}
    }
}