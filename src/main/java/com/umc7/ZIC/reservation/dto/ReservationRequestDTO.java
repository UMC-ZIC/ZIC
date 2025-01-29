package com.umc7.ZIC.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc7.ZIC.practiceRoom.validation.annotation.CheckPracticeRoomDetailStatus;
import com.umc7.ZIC.practiceRoom.validation.annotation.ExistPracticeRoomDetail;
import com.umc7.ZIC.reservation.validation.annotation.CheckReservationTime;
import com.umc7.ZIC.reservation.validation.annotation.CheckReservationTimeOverlap;
import com.umc7.ZIC.reservation.validation.validationSequence.ValidationStep;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationRequestDTO {
    /**
     * 연습실 예약 시 클라이언트에서 정보를 받아오는 DTO
     * @param reservationNumber 클라이언트에서 생성한 무작위 문자열
     * @param practiceRoomDetail
     * @param date
     * @param startTime
     * @param endTime
     */
    @Builder
    @CheckReservationTime(groups = ValidationStep.timeValidationStep1.class)
    @CheckReservationTimeOverlap(groups = ValidationStep.timeValidationStep2.class)
    public record reservationRegistDTO(
            String reservationNumber,
            @ExistPracticeRoomDetail(groups = ValidationStep.ExistValidation.class)
            @CheckPracticeRoomDetailStatus(groups = ValidationStep.StatusValidation.class)
            Long practiceRoomDetail,
            LocalDate date,
            @DateTimeFormat(pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,
            @DateTimeFormat(pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime endTime
    ) {}
}
