package com.umc7.ZIC.reservation.controller;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.common.validation.annotation.CheckPage;
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
import com.umc7.ZIC.reservation.validation.validationSequence.ValidationOrder;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.domain.enums.RoleType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Validated
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
    @GetMapping("/user")
    public ApiResponse<ReservationResponseDTO.ReservationList> getReservationList(
            @RequestParam(name = "date") LocalDate date,
            @CheckPage @RequestParam(name = "page") Integer page
    ) {
        Page<Reservation> reservationList = reservationQueryService.getReservationList(jwtTokenProvider.getUserIdFromToken(), date, page - 1);

        return ApiResponse.onSuccess(ReservationConverter.toReservationList(reservationList));
    }

    @Operation(summary = "예약 요청 API",
            description = "연습실 예약 결제 요청하는 API입니다. " +
                    "<br>startTime과 endTime은 <b>\"HH:mm\"</b> 형식으로 입력해주세요. ex) startTime=\"02:00\", endTime=\"13:30\". " +
                    "<br>partner_order_id는 프론트엔드에서 생성한 무작위 문자열입니다." +
                    "<br><h2>JWT 토큰 필요합니다.</h2> ")
    @PostMapping("/payment/kakao/ready")
    public ApiResponse<ReservationResponseDTO.reservationDTO<Object>> readyToKakaoPay(
            @RequestBody @Validated(ValidationOrder.OrderedReservationValidation.class) ReservationRequestDTO.reservationRegistDTO request) {
        PaymentResponseDTO.KakaoPaymentReadyResponseDTO kakaoPaymentReadyResponseDTO = kakaoPayService.kakaoPayReady(request, jwtTokenProvider.getUserIdFromToken());
        Reservation reservation = reservationCommandService.registReservation(request, jwtTokenProvider.getUserIdFromToken());
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
    public ApiResponse<PaymentResponseDTO.KakaoPaymentResultDTO<Object>> approveToKakaoPay(
            @RequestBody @Validated(ValidationOrder.OrderedKakaoPaymentValidation.class) PaymentRequestDTO.KakaoPaymentApproveRequestDTO request) {
        PaymentResponseDTO.KakaoPaymentApproveResponseDTO result = kakaoPayService.kakaoPayApprove(request, jwtTokenProvider.getUserIdFromToken());
        ReservationDetail reservationDetail = reservationCommandService.registReservationDetail(request, result);

        return ApiResponse.onSuccess(ReservationConverter.toPaymentDTO(reservationDetail, result));
    }

    @Operation(summary = "결제 취소 API",
            description = "KaKao Pay 결제 취소 API입니다. " +
                    "<br><h2>JWT 토큰 필요합니다.</h2> ")
    @PatchMapping("/payment/kakao/cancel")
    public ApiResponse<ReservationResponseDTO.reservationDTO<PaymentResponseDTO.KakaoPaymentCancelResponseDTO>> cancelToKakaoPay(
            @RequestBody @Validated(ValidationOrder.OrderedKakaoPaymentValidation.class) PaymentRequestDTO.KakaoPaymentCancelRequestDTO request) {

        return ApiResponse.onSuccess(kakaoPayService.kakaoPayCancel(request));
    }

    @Operation(summary = "대여자의 예약 목록 조회 API",
            description = "특정 대여자가 등록한 연습실의 예약 목록을 조회하는 API입니다. (Owner 타입 유저)" +
                    "<br>query String 으로 날짜와 page 번호를 주세요" +
                    "<br><h2>JWT 토큰 필요합니다.</h2> ")
    @Parameters({
            @Parameter(name = "date", description = "조회할 예약 날짜, yyyy-MM-dd 형식으로 입력 ex) 2025-01-01, query String 입니다!"),
            @Parameter(name = "page", description = "페이지 번호, 1 이상의 숫자 입력, query String 입니다!")
    })
    @GetMapping("/owner")
    public ApiResponse<ReservationResponseDTO.OwnerReservationListDTO> getOwnerReservationList(  // OwnerReservationListDTO
                                                                                                 @RequestParam(name = "date") LocalDate date,
                                                                                                 @CheckPage @RequestParam(name = "page") Integer page
    ) {
        Page<Reservation> reservationList = reservationQueryService.getOwnerReservationList(jwtTokenProvider.getUserIdFromToken(), date, page - 1);

        return ApiResponse.onSuccess(ReservationConverter.toOwnerReservationList(reservationList)); // Owner 전용 Converter 사용
    }

    @Operation(summary = "이용자의 특정 달에 예약한 날짜 목록 조회 API",
            description = "특정 이용자가 특정한 달에 예약한 날짜의 목록을 조회하는 API입니다." +
                    "<br><h2>JWT 토큰 필요합니다.</h2> ")
    @Parameters({
            @Parameter(name = "date", description = "조회할 예약 날짜, yyyy-MM-dd 형식으로 입력 ex) 2025-01-01, query String 입니다!"),
    })
    @GetMapping("/user/date")
    public ApiResponse<ReservationResponseDTO.ReservationMonthUserDTO> getUserReservationDateList(
                                                                                                 @RequestParam(name = "date") LocalDate date
    ) {
        if (!Objects.equals(jwtTokenProvider.getUserTypeFromToken(), RoleType.USER.name())) {
            throw new UserHandler(ErrorStatus.JWT_AUTHORIZATION_FAILED);
        }

        return ApiResponse.onSuccess(reservationQueryService.getUserReservationMonth(jwtTokenProvider.getUserIdFromToken(), jwtTokenProvider.getUserTypeFromToken(), date));
    }

    @Operation(summary = "대여자가 등록한 연습실에 특정 달 동안 예약한 날짜 목록 조회 API",
            description = "특정 대여자가 특정한 달 동안 본인 연습실에 예약한 날짜의 목록을 조회하는 API입니다." +
                    "<br><h2>JWT 토큰 필요합니다.</h2> ")
    @Parameters({
            @Parameter(name = "date", description = "조회할 예약 날짜, yyyy-MM-dd 형식으로 입력 ex) 2025-01-01, query String 입니다!"),
    })
    @GetMapping("/owner/date")
    public ApiResponse<ReservationResponseDTO.ReservationMonthOwnerDTO> getOwnerReservationDateList(
            @RequestParam(name = "date") LocalDate date
    ) {
        if (!Objects.equals(jwtTokenProvider.getUserTypeFromToken(), RoleType.OWNER.name())) {
            throw new UserHandler(ErrorStatus.JWT_AUTHORIZATION_FAILED);
        }

        return ApiResponse.onSuccess(reservationQueryService.getOwnerReservationMonth(jwtTokenProvider.getUserIdFromToken(), jwtTokenProvider.getUserTypeFromToken(), date));
    }
}
