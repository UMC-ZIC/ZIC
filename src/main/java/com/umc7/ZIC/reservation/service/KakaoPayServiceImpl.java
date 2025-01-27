package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomDetailRepository;
import com.umc7.ZIC.reservation.converter.KakaoPayConverter;
import com.umc7.ZIC.reservation.dto.PaymentRequestDTO;
import com.umc7.ZIC.reservation.dto.PaymentResponseDTO;
import com.umc7.ZIC.reservation.dto.ReservationRequestDTO;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayServiceImpl implements KakaoPayService {
    private final PracticeRoomDetailRepository practiceRoomDetailRepository;
    private final UserRepository userRepository;

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
        
        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findById(request.practiceRoomDetail()).get();
        User user = userRepository.findById(userid).get();
        String itemName = practiceRoomDetail.getPracticeRoom().getName() + " " + practiceRoomDetail.getName();

        // JSON BODY 생성
        Map<String, String> parameters = KakaoPayConverter.toReadyParam(
                cid, request.reservationNumber(), user.getName(), itemName, request.startTime(), request.endTime(), request.price(), kakaoPayRedirectUrl
        );

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders(secret_Key_Dev));
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
        User user = userRepository.findById(userid).get();

        Map<String, String> parameters = KakaoPayConverter.toApproveParam(cid, request, user.getName());
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders(secret_Key_Dev));
        RestTemplate restTemplate = new RestTemplate();

        // KAKAO 결제 승인 보내기
        kakaoPaymentResponseDTO = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                requestEntity,
                PaymentResponseDTO.KakaoPaymentApproveResponseDTO.class
        );

        return kakaoPaymentResponseDTO;
    }

    // JSON 헤더 추가
    private HttpHeaders getHeaders(String secret_Key_Dev) {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "SECRET_KEY " + secret_Key_Dev;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-Type", "application/json");

        return httpHeaders;
    }
}
