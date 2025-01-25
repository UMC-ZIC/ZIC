package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.reservation.dto.PaymentRequestDTO;
import com.umc7.ZIC.reservation.dto.PaymentResponseDTO;
import com.umc7.ZIC.reservation.dto.ReservationRequestDTO;

public interface KakaoPayService {

    /**
     * 카카오 페이 결제 요청 Service
     * @param request
     * @param userid
     * @return
     */
    PaymentResponseDTO.KakaoPaymentReadyResponseDTO kakaoPayReady(ReservationRequestDTO.reservationRegistDTO request, Long userid);

    /**
     * 카카오 페이 결제 승인 Service
     * @param request
     * @param userid
     * @return
     */
    PaymentResponseDTO.KakaoPaymentApproveResponseDTO kakaoPayApprove(PaymentRequestDTO.KakaoPaymentApproveRequestDTO request, Long userid);
}
