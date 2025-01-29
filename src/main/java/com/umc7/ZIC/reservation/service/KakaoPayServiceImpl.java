package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomDetailHandler;
import com.umc7.ZIC.apiPayload.exception.handler.ReservationHandler;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomDetailRepository;
import com.umc7.ZIC.reservation.converter.KakaoPayConverter;
import com.umc7.ZIC.reservation.converter.ReservationConverter;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.Status;
import com.umc7.ZIC.reservation.dto.PaymentRequestDTO;
import com.umc7.ZIC.reservation.dto.PaymentResponseDTO;
import com.umc7.ZIC.reservation.dto.ReservationRequestDTO;
import com.umc7.ZIC.reservation.dto.ReservationResponseDTO;
import com.umc7.ZIC.reservation.repository.ReservationRepository;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayServiceImpl implements KakaoPayService {
    private final PracticeRoomDetailRepository practiceRoomDetailRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    // KaKao Pay 가맹점 테스트 코드 : TC0ONETIME
    @Value("${KAKAO_PAY_CID}")
    private String cid;

    // KaKao Pay Secret_Key_DEV
    @Value("${KAKAO_PAY_SECRET_KEY}")
    private String secret_Key_Dev;

    // KaKao Pay Redirect URL
    @Value("${KAKAO_PAY_REDIRECT_URL}")
    private String kakaoPayRedirectUrl;

    @Override
    public PaymentResponseDTO.KakaoPaymentReadyResponseDTO kakaoPayReady(ReservationRequestDTO.reservationRegistDTO request, Long userid) {
        PaymentResponseDTO.KakaoPaymentReadyResponseDTO kakaoPaymentResponseDTO;
        
        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findById(request.practiceRoomDetail())
                .orElseThrow(() -> new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND));
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        String itemName = practiceRoomDetail.getPracticeRoom().getName() + " " + practiceRoomDetail.getName();

        // JSON BODY 생성
        Map<String, String> parameters = KakaoPayConverter.toReadyParam(
                cid, request.reservationNumber(), user.getName(), itemName, request.startTime(), request.endTime(), practiceRoomDetail.getFee(), kakaoPayRedirectUrl
        );

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, KakaoPayConverter.getHeaders(secret_Key_Dev));
        RestTemplate restTemplate = new RestTemplate();

        // KAKAO PAY로 결제 요청 보내기
        kakaoPaymentResponseDTO = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                PaymentResponseDTO.KakaoPaymentReadyResponseDTO.class
        );

        return kakaoPaymentResponseDTO;
    }

    @Override
    public PaymentResponseDTO.KakaoPaymentApproveResponseDTO kakaoPayApprove(PaymentRequestDTO.KakaoPaymentApproveRequestDTO request, Long userid) {
        PaymentResponseDTO.KakaoPaymentApproveResponseDTO kakaoPaymentResponseDTO;
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Map<String, String> parameters = KakaoPayConverter.toApproveParam(cid, request, user.getName());
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, KakaoPayConverter.getHeaders(secret_Key_Dev));
        RestTemplate restTemplate = new RestTemplate();

        // KAKAO 결제 승인 보내기
        kakaoPaymentResponseDTO = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                PaymentResponseDTO.KakaoPaymentApproveResponseDTO.class
        );

        return kakaoPaymentResponseDTO;
    }

    @Override
    public ReservationResponseDTO.reservationDTO<PaymentResponseDTO.KakaoPaymentCancelResponseDTO> kakaoPayCancel(PaymentRequestDTO.KakaoPaymentCancelRequestDTO request) {
        PaymentResponseDTO.KakaoPaymentCancelResponseDTO kakaoPaymentCancelResponseDTO;
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ReservationHandler(ErrorStatus.RESERVATION_NOT_FOUND));

        Map<String, String> parameters = KakaoPayConverter.toCancelParam(cid, request);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, KakaoPayConverter.getHeaders(secret_Key_Dev));
        RestTemplate restTemplate = new RestTemplate();

        // KAKAO 결제 승인 보내기
        kakaoPaymentCancelResponseDTO = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/cancel",
                requestEntity,
                PaymentResponseDTO.KakaoPaymentCancelResponseDTO.class
        );

        Reservation toggleReservation = ReservationConverter.toReservationToggle(reservation, Status.CANCEL);
        Reservation newReservation = reservationRepository.save(toggleReservation);

        return ReservationConverter.toReservationDTO(kakaoPaymentCancelResponseDTO, ReservationConverter.toReservationResult(newReservation));
    }
}
