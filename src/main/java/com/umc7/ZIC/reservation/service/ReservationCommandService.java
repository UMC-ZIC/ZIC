package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.ReservationDetail;
import com.umc7.ZIC.reservation.domain.enums.Status;
import com.umc7.ZIC.reservation.dto.PaymentRequestDTO;
import com.umc7.ZIC.reservation.dto.PaymentResponseDTO;
import com.umc7.ZIC.reservation.dto.ReservationRequestDTO;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 예약 엔티티에서 Status가 PENDING인 데이터들을 FAIL로 변경하는 Service
     * @param time
     * @param status
     * @return
     */
    List<Reservation> reservationToggleStatus(LocalDateTime time, Status status);
}
