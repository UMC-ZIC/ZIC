package com.umc7.ZIC.reservation.controller;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.reservation.converter.ReservationConverter;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.ReservationDetail;
import com.umc7.ZIC.reservation.dto.PaymentRequestDTO;
import com.umc7.ZIC.reservation.dto.PaymentResponseDTO;
import com.umc7.ZIC.reservation.dto.ReservationRequestDTO;
import com.umc7.ZIC.reservation.dto.ReservationResponseDTO;
import com.umc7.ZIC.reservation.service.KakaoPayService;
import com.umc7.ZIC.reservation.service.ReservationCommandService;
import com.umc7.ZIC.reservation.service.ReservationQueryService;
import com.umc7.ZIC.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
@Tag(name = "예약", description = "예약 API")
public class ReservationRestController {
    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;
    private final KakaoPayService kakaoPayService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "유저의 특정 날짜에 예약한 리스트 조회 API",
            description = "특정 유저가 예약한 예약 목록을 조회하는 API이며, 페이징을 포함합니다. " +
                    "<br>query String 으로 날짜와 page 번호를 주세요" +
                    "<br><h2>JWT 토큰 필요합니다.</h2> ")
    @Parameters({
            @Parameter(name = "date", description = "조회할 예약 날짜, yyyy-MM-dd 형식으로 입력 ex) 2025-01-01, query String 입니다!"),
            @Parameter(name = "page", description = "페이지 번호, 1 이상의 숫자 입력, query String 입니다!")
    })
    @GetMapping("")
    public ApiResponse<ReservationResponseDTO.ReservationList> getReservationList(
            @RequestParam(name = "date") LocalDate date,
            @RequestParam(name = "page") Integer page
    ) {
        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        Page<Reservation> reservationList = reservationQueryService.getReservationList(userId, date, page - 1);

        return ApiResponse.onSuccess(ReservationConverter.toReservationList(reservationList));
    }

    @Operation(summary = "예약 요청 API",
            description = "연습실 예약 결제 요청하는 API입니다. " +
                    "<br>startTime과 endTime은 <b>\"HH:mm\"</b> 형식으로 입력해주세요. ex) startTime=\"02:00\", endTime=\"13:30\". " +
                    "<br>partner_order_id는 프론트엔드에서 생성한 무작위 문자열입니다." +
                    "<br><h2>JWT 토큰 필요합니다.</h2> ")
    @PostMapping("/payment/kakao/ready")
    public ApiResponse<ReservationResponseDTO.reservationDTO<Object>> readyToKakaoPay(@RequestBody ReservationRequestDTO.reservationRegistDTO request) {
        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }
        Long userId = jwtTokenProvider.getUserIdFromToken();

        PaymentResponseDTO.KakaoPaymentReadyResponseDTO kakaoPaymentReadyResponseDTO = kakaoPayService.kakaoPayReady(request, userId);
        Reservation reservation = reservationCommandService.registReservation(request, userId);
        ReservationResponseDTO.reservationResultDTO reservationResult = ReservationConverter.toReservationResult(reservation);

        return ApiResponse.onSuccess(ReservationConverter.toReservationDTO(kakaoPaymentReadyResponseDTO, reservationResult));
    }

    @Operation(summary = "결제 승인 API",
            description = "KaKao Pay 결제 승인 API입니다. " +
                    "<br>tid : 예약 요청으로 받은 결제 번호. " +
                    "<br>partner_order_id : 프론트엔드에서 생성한 무작위 문자열. " +
                    "<br>pg_token : 예약 요청으로 url param을 통해 받은 토큰. " +
                    "<br><h2>JWT 토큰 필요합니다.</h2> ")
    @PostMapping("/payment/kakao/approve")
    public ApiResponse<PaymentResponseDTO.KakaoPaymentResultDTO<Object>> approveToKakaoPay(@RequestBody PaymentRequestDTO.KakaoPaymentApproveRequestDTO request) {
        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }
        Long userId = jwtTokenProvider.getUserIdFromToken();

        PaymentResponseDTO.KakaoPaymentApproveResponseDTO result = kakaoPayService.kakaoPayApprove(request, userId);
        ReservationDetail reservationDetail = reservationCommandService.registReservationDetail(request, result);

        return ApiResponse.onSuccess(ReservationConverter.toPaymentDTO(reservationDetail, result));
    }

    @Operation(summary = "결제 취소 API",
            description = "KaKao Pay 결제 취소 API입니다. " +
                    "<br><h2>JWT 토큰 필요합니다.</h2> ")
    @PostMapping("/payment/kakao/cancel")
    public ApiResponse<ReservationResponseDTO.reservationDTO<PaymentResponseDTO.KakaoPaymentCancelResponseDTO>> cancelToKakaoPay(@RequestBody PaymentRequestDTO.KakaoPaymentCancelRequestDTO request) {
        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        return ApiResponse.onSuccess(kakaoPayService.kakaoPayCancel(request));
    }
}
