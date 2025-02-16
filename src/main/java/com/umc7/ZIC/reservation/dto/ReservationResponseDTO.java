package com.umc7.ZIC.reservation.dto;

import com.umc7.ZIC.reservation.domain.enums.ReservationStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationResponseDTO {

    /**
     * 연습실 예약 결과를 가져오는 DTO
     * @param id
     * @param reservationNumber
     * @param practiceRoomDetail
     * @param user
     * @param status
     * @param date
     * @param startTime
     * @param endTime
     */
    @Builder
    public record reservationResultDTO(
            Long id,
            String reservationNumber,
            Long practiceRoomDetail,
            Long user,
            ReservationStatus status,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {}

    /**
     * 예약 결과와 결제 승인 리다이렉트 URL을 전달하는 DTO
     * @param reservationResult 연습실 예약 결과
     * @param paymentResponse 결제 승인 응답
     * @param <T> paymentResponse를 받아올 DTO 타입
     */
    @Builder
    public record reservationDTO<T>(
            reservationResultDTO reservationResult,
            T paymentResponse
    ) {}

    /**
     * DB에서 조회한 예약 내역 및 상세 내역 목록 객체
     * @param reservationResult
     * @param reservationDetailResult
     */
    @Builder
    public record ReservationListDTO(
            ReservationDTO reservationResult,
            ReservationDetailDTO reservationDetailResult
    ) {
        @Builder
        public record ReservationDTO(
                Long id,
                String reservationNumber,
                PracticeRoomDTO practiceRoom,
                PracticeRoomDetailDTO practiceRoomDetail,
                ReservationStatus status,
                Long user,
                LocalDate date,
                LocalTime startTime,
                LocalTime endTime
        ) {
            @Builder
            public record PracticeRoomDTO(
                    Long PracticeRoomId,
                    String PracticeRoomName,
                    Long PracticeRoomOwnerId,
                    String PracticeRoomOwnerName,
                    Address address

            ) {
                @Builder
                public record Address(
                        String region,
                        String address
                ) {}
            }

            @Builder
            public record PracticeRoomDetailDTO(
                    Long practiceRoomDetailId,
                    String practiceRoomDetailName,
                    String practiceRoomDetailImage
            ) {}
        }

        @Builder
        public record ReservationDetailDTO(
                String tid,
                Integer amount,
                Integer tax_free_amount,
                Integer vat_amount
        ) {}
    }

    /**
     * 클라이언트에게 전달할 애약 목록
     * @param resultList
     * @param listSize
     * @param totalPage
     * @param totalElements
     * @param isFirst
     * @param isLast
     */
    @Builder
    public record ReservationList(
            List<ReservationListDTO> resultList,
            Integer listSize,
            Integer totalPage,
            Long totalElements,
            Boolean isFirst,
            Boolean isLast
    ) {}
    // Owner 전용 예약 목록 DTO
    @Builder
    public record OwnerReservationListDTO( // 추가: Owner 전용 예약 목록 DTO
                                           List<OwnerReservationDTO> resultList,
                                           Integer listSize,
                                           Integer totalPage,
                                           Long totalElements,
                                           Boolean isFirst,
                                           Boolean isLast
    ) {}


    // Owner 전용 예약 정보 DTO
    @Builder
    public record OwnerReservationDTO( // 추가: Owner 전용 예약 정보 DTO
                                       Long id,
                                       String reservationNumber,
                                       PracticeRoomDTO practiceRoom,
                                       PracticeRoomDetailDTO practiceRoomDetail,
                                       ReservationStatus status,
                                       Long user, // 대여자의 ID
                                       LocalDate date,
                                       LocalTime startTime,
                                       LocalTime endTime,
                                       Long practiceRoomId,
                                       Long practiceRoomDetailId,
                                       String practiceRoomDetailName,
                                       String reservationUserName,
                                       Integer amount
    ){
        // (내부 PracticeRoomDTO, PracticeRoomDetailDTO는 이전과 동일)
        @Builder
        public record PracticeRoomDTO(
                Long PracticeRoomId,
                String PracticeRoomName,
                Long PracticeRoomOwnerId,
                String PracticeRoomOwnerName,
                Address address

        ) {
            @Builder
            public record Address(
                    String region,
                    String address
            ) {}
        }

        @Builder
        public record PracticeRoomDetailDTO(
                Long practiceRoomDetailId,
                String practiceRoomDetailName,
                String practiceRoomDetailImage
        ) {}
    }

    /**
     * 특정 달동안 예약된 날짜 목록
     * @param userId
     * @param role
     * @param date
     * @param reservationDateList
     */
    @Builder
    public record ReservationMonthUserDTO(
            Long userId,
            String role,
            LocalDate date,
            List<LocalDate> reservationDateList
    ) {}

    /**
     * 대여자가 등록한 연습실에 특정 달 동안 예약한 예약 날짜 목록
     * @param userId
     * @param role
     * @param date
     * @param reservationDateList
     */
    @Builder
    public record ReservationMonthOwnerDTO(
            Long userId,
            String role,
            LocalDate date,
            List<LocalDate> reservationDateList
    ) {}
}

