package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.ReservationDetail;
import com.umc7.ZIC.reservation.dto.PaymentRequestDTO;
import com.umc7.ZIC.reservation.dto.PaymentResponseDTO;
import com.umc7.ZIC.reservation.dto.ReservationRequestDTO;

public interface ReservationCommandService {

    /**
     * Reservation 엔티티에 데이터를 저장하는 Service
     * @param request
     * @param userId
     * @return
     */
    Reservation registReservation(ReservationRequestDTO.reservationRegistDTO request, Long userId);

    /**
     * ReservationDetail 엔티티에 데이터를 저장하는 Service
     * @param requestDTO KAKAO PAY 결제 승인 요청 DTO
     * @param responseDTO KAKAO PAY 결제 승인 응답 DTO
     * @return ReservationDetail
     */
    ReservationDetail registReservationDetail(PaymentRequestDTO.KakaoPaymentApproveRequestDTO requestDTO, PaymentResponseDTO.KakaoPaymentApproveResponseDTO responseDTO);
}
