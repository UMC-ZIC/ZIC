package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomDetailHandler;
import com.umc7.ZIC.apiPayload.exception.handler.ReservationHandler;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomDetailRepository;
import com.umc7.ZIC.reservation.converter.ReservationConverter;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.ReservationDetail;
import com.umc7.ZIC.reservation.domain.enums.ReservationStatus;
import com.umc7.ZIC.reservation.dto.PaymentRequestDTO;
import com.umc7.ZIC.reservation.dto.PaymentResponseDTO;
import com.umc7.ZIC.reservation.dto.ReservationRequestDTO;
import com.umc7.ZIC.reservation.repository.ReservationDetailRepository;
import com.umc7.ZIC.reservation.repository.ReservationRepository;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationCommandServiceImpl implements ReservationCommandService {
    private final ReservationRepository reservationRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final PracticeRoomDetailRepository practiceRoomDetailRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Reservation registReservation(ReservationRequestDTO.reservationRegistDTO request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findById(request.practiceRoomDetail())
                .orElseThrow(() -> new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND));
        Reservation newReservation = ReservationConverter.toReservationRegist(request, practiceRoomDetail, user);

        return reservationRepository.save(newReservation);
    }

    @Override
    public ReservationDetail registReservationDetail(
            PaymentRequestDTO.KakaoPaymentApproveRequestDTO requestDTO, PaymentResponseDTO.KakaoPaymentApproveResponseDTO responseDTO) {
        Reservation reservation = reservationRepository.findById(requestDTO.reservationId())
                .orElseThrow(() -> new ReservationHandler(ErrorStatus.RESERVATION_NOT_FOUND));
        Reservation toggleReservation = ReservationConverter.toReservationToggle(reservation, ReservationStatus.SUCCESS);

        Reservation newReservation = reservationRepository.save(toggleReservation);
        ReservationDetail newReservationDetail = ReservationConverter.toReservationDetail(responseDTO, newReservation);

        return reservationDetailRepository.save(newReservationDetail);
    }

    @Override
    public List<Reservation> reservationToggleStatus(LocalDateTime time, ReservationStatus status) {
        List<Reservation> reservationList = reservationRepository.findByStatusAndCreatedAtBefore(ReservationStatus.PENDING, time);

        List<Reservation> newReservationList = ReservationConverter.toReservationListToggle(reservationList, status);

        return reservationRepository.saveAll(newReservationList);
    }
}
