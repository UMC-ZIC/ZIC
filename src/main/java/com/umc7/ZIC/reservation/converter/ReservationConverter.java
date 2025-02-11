package com.umc7.ZIC.reservation.converter;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.ReservationDetail;
import com.umc7.ZIC.reservation.domain.enums.ReservationStatus;
import com.umc7.ZIC.reservation.dto.PaymentResponseDTO;
import com.umc7.ZIC.reservation.dto.ReservationRequestDTO;
import com.umc7.ZIC.reservation.dto.ReservationResponseDTO;
import com.umc7.ZIC.user.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;

public class ReservationConverter {

    /**
     * Reservation 엔티티에 저장할 객체 생성
     * @param request
     * @param practiceRoomDetail
     * @param user
     * @return
     */
    public static Reservation toReservationRegist(ReservationRequestDTO.reservationRegistDTO request, PracticeRoomDetail practiceRoomDetail, User user) {
        return Reservation.builder()
                .reservationNumber(request.reservationNumber())
                .practiceRoomDetail(practiceRoomDetail)
                .user(user)
                .date(request.date())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .build();
    }

    /**
     * Reservation 엔티티에서 예약 데이터의 status를 변경
     * @param reservation
     * @param status
     * @return
     */
    public static Reservation toReservationToggle(Reservation reservation, ReservationStatus status) {
        return reservation.toggleStatus(status);
    }

    /**
     * Reservation 엔티티에서 예약 데이터들의 status를 변경
     * @param reservationList
     * @param status
     * @return
     */
    public static List<Reservation> toReservationListToggle(List<Reservation> reservationList, ReservationStatus status) {
        return reservationList.stream().map(reservation -> toReservationToggle(reservation, status)).toList();
    }

    /**
     * Reservation 엔티티에 저장 후 결과를 알려줄 객체 생성
     * @param reservation
     * @return
     */
    public static ReservationResponseDTO.reservationResultDTO toReservationResult(Reservation reservation) {
        return ReservationResponseDTO.reservationResultDTO.builder()
                .id(reservation.getId())
                .reservationNumber(reservation.getReservationNumber())
                .practiceRoomDetail(reservation.getPracticeRoomDetail().getId())
                .user(reservation.getUser().getId())
                .status(reservation.getStatus())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .build();
    }

    /**
     * 클라이언트에게 보여줄 Reservation 엔티티 저장 결과 + KAKAO PAY 결제 요청 응답
     * @param payment KAKAO PAY 결제 요청 응답
     * @param reservation Reservation 엔티티 저장 결과
     * @return 위 두 객체를 합친 DTO
     * @param <T> 결제 요청 응답을 받을 DTO의 종류
     */
    public static <T> ReservationResponseDTO.reservationDTO<T> toReservationDTO(T payment, ReservationResponseDTO.reservationResultDTO reservation) {
        return ReservationResponseDTO.reservationDTO.<T>builder()
                .reservationResult(reservation)
                .paymentResponse(payment)
                .build();
    }

    /**
     * Reservation Detail 엔티티에 저장할 객체 생성
     * @param paymentResult 결제 승인 후 받은 응답
     * @param reservation Reservation 엔티티로 부터 조회한 예약 객체
     * @return ReservationDetail
     */
    public static ReservationDetail toReservationDetail(PaymentResponseDTO.KakaoPaymentApproveResponseDTO paymentResult, Reservation reservation) {
        return ReservationDetail.builder()
                .reservation(reservation)
                .tid(paymentResult.tid())
                .amount(paymentResult.amount().total())
                .tax_free_amount(paymentResult.amount().tax_free())
                .vat_amount(paymentResult.amount().vat())
                .build();
    }

    /**
     * 클라이언트에게 보여줄 ReservationDetail 엔티티에 저장한 결과 + KAKAO PAY 결제 승인 응답
     * @param reservationDetail ReservationDetail 엔티티에 저장한 결과 id
     * @param paymentResult KAKAO PAY 결제 승인 응답
     * @return 두 객체를 합친 DTO
     * @param <T> 결제 승인 응답을 받을 DTO 종류
     */
    public static <T> PaymentResponseDTO.KakaoPaymentResultDTO<T> toPaymentDTO(ReservationDetail reservationDetail, T paymentResult) {
        return PaymentResponseDTO.KakaoPaymentResultDTO.<T>builder()
                .reservation_detail_id(reservationDetail.getId())
                .PaymentResult(paymentResult)
                .build();
    }

    /**
     * 예약 목록 생성
     * @param reservationList
     * @return
     */
    public static ReservationResponseDTO.ReservationList toReservationList(Page<Reservation> reservationList) {
        List<ReservationResponseDTO.ReservationListDTO> newReservationList = reservationList.stream().map(ReservationConverter::toReservationDetailResult).toList();

        return ReservationResponseDTO.ReservationList.builder()
                .resultList(newReservationList)
                .listSize(newReservationList.size())
                .totalPage(reservationList.getTotalPages())
                .totalElements(reservationList.getTotalElements())
                .isFirst(reservationList.isFirst())
                .isLast(reservationList.isLast())
                .build();
    }

    /**
     * 예약 정보와 예약 상세정보 객체를 생성
     * @param reservation
     * @return
     */
    public static ReservationResponseDTO.ReservationListDTO toReservationDetailResult(Reservation reservation) {
        // 연습실 건물 정보 객체 생성
        ReservationResponseDTO.ReservationListDTO.ReservationDTO.PracticeRoomDTO practiceRoomDTO
                = ReservationResponseDTO.ReservationListDTO.ReservationDTO.PracticeRoomDTO.builder()
                        .PracticeRoomId(reservation.getPracticeRoomDetail().getPracticeRoom().getId())
                        .PracticeRoomName(reservation.getPracticeRoomDetail().getPracticeRoom().getName())
                        .PracticeRoomOwnerId(reservation.getPracticeRoomDetail().getPracticeRoom().getUser().getId())
                        .PracticeRoomOwnerName(reservation.getPracticeRoomDetail().getPracticeRoom().getUser().getName())
                        .address(ReservationResponseDTO.ReservationListDTO.ReservationDTO.PracticeRoomDTO.Address.builder()
                                .region(reservation.getPracticeRoomDetail().getPracticeRoom().getRegion().getName().getKoreanName())
                                .address(reservation.getPracticeRoomDetail().getPracticeRoom().getAddress())
                                .build())
                        .build();
        // 연습실 방 상세 정보 객체 생성
        ReservationResponseDTO.ReservationListDTO.ReservationDTO.PracticeRoomDetailDTO practiceRoomDetailDTO
                = ReservationResponseDTO.ReservationListDTO.ReservationDTO.PracticeRoomDetailDTO.builder()
                        .practiceRoomDetailId(reservation.getPracticeRoomDetail().getId())
                        .practiceRoomDetailName(reservation.getPracticeRoomDetail().getName())
                        .practiceRoomDetailImage(reservation.getPracticeRoomDetail().getImage())
                        .build();

        return ReservationResponseDTO.ReservationListDTO.builder()
                .reservationResult(ReservationResponseDTO.ReservationListDTO.ReservationDTO.builder()
                        .id(reservation.getId())
                        .reservationNumber(reservation.getReservationNumber())
                        .practiceRoom(practiceRoomDTO)
                        .practiceRoomDetail(practiceRoomDetailDTO)
                        .user(reservation.getUser().getId())
                        .status(reservation.getStatus())
                        .date(reservation.getDate())
                        .startTime(reservation.getStartTime())
                        .endTime(reservation.getEndTime())
                        .build())
                .reservationDetailResult(ReservationResponseDTO.ReservationListDTO.ReservationDetailDTO.builder()
                        .tid(reservation.getReservationDetail().getTid())
                        .amount(reservation.getReservationDetail().getAmount())
                        .tax_free_amount(reservation.getReservationDetail().getTax_free_amount())
                        .vat_amount(reservation.getReservationDetail().getVat_amount())
                        .build())
                .build();
    }


    //Owner 전용 변환 메서드 추가
    // Page<Reservation>을 OwnerReservationListDTO로 변환하는 메서드
    public static ReservationResponseDTO.OwnerReservationListDTO toOwnerReservationList(Page<Reservation> reservationList) { // 추가

        List<ReservationResponseDTO.OwnerReservationDTO> ownerReservations = reservationList.stream()
                .map(ReservationConverter::toOwnerReservationDetailResult).toList(); // OwnerReservationDTO로 변환

        return ReservationResponseDTO.OwnerReservationListDTO.builder() // OwnerReservationListDTO 생성
                .resultList(ownerReservations)
                .listSize(ownerReservations.size())
                .totalPage(reservationList.getTotalPages())
                .totalElements(reservationList.getTotalElements())
                .isFirst(reservationList.isFirst())
                .isLast(reservationList.isLast())
                .build();
    }

    // Reservation 객체를 OwnerReservationDTO로 변환하는 메서드
    public static ReservationResponseDTO.OwnerReservationDTO toOwnerReservationDetailResult(Reservation reservation) { // 추가
        // 기존 PracticeRoomDTO, PracticeRoomDetailDTO 생성 로직
        ReservationResponseDTO.OwnerReservationDTO.PracticeRoomDTO practiceRoomDTO
                = ReservationResponseDTO.OwnerReservationDTO.PracticeRoomDTO.builder()
                .PracticeRoomId(reservation.getPracticeRoomDetail().getPracticeRoom().getId())
                .PracticeRoomName(reservation.getPracticeRoomDetail().getPracticeRoom().getName())
                .PracticeRoomOwnerId(reservation.getPracticeRoomDetail().getPracticeRoom().getUser().getId())
                .PracticeRoomOwnerName(reservation.getPracticeRoomDetail().getPracticeRoom().getUser().getName())
                .address(ReservationResponseDTO.OwnerReservationDTO.PracticeRoomDTO.Address.builder()
                        .region(reservation.getPracticeRoomDetail().getPracticeRoom().getRegion().getName().getKoreanName())
                        .address(reservation.getPracticeRoomDetail().getPracticeRoom().getAddress())
                        .build())
                .build();

        ReservationResponseDTO.OwnerReservationDTO.PracticeRoomDetailDTO practiceRoomDetailDTO
                = ReservationResponseDTO.OwnerReservationDTO.PracticeRoomDetailDTO.builder()
                .practiceRoomDetailId(reservation.getPracticeRoomDetail().getId())
                .practiceRoomDetailName(reservation.getPracticeRoomDetail().getName())
                .practiceRoomDetailImage(reservation.getPracticeRoomDetail().getImage())
                .build();

        return ReservationResponseDTO.OwnerReservationDTO.builder() // OwnerReservationDTO 생성
                .id(reservation.getId())
                .reservationNumber(reservation.getReservationNumber())
                .practiceRoom(practiceRoomDTO)
                .practiceRoomDetail(practiceRoomDetailDTO)
                .user(reservation.getUser().getId()) // 필요에 따라 대여자의 ID 또는 예약한 사용자의 ID
                .status(reservation.getStatus())
                .date(reservation.getDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .practiceRoomId(reservation.getPracticeRoomDetail().getPracticeRoom().getId())
                .practiceRoomDetailId(reservation.getPracticeRoomDetail().getId())
                .practiceRoomDetailName(reservation.getPracticeRoomDetail().getName())
                .reservationUserName(reservation.getUser().getName())
                .amount(reservation.getReservationDetail().getAmount())
                .build();
    }
}
