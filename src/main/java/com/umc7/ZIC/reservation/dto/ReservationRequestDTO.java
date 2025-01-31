package com.umc7.ZIC.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc7.ZIC.practiceRoom.validation.annotation.CheckPracticeRoomDetailStatus;
import com.umc7.ZIC.practiceRoom.validation.annotation.ExistPracticeRoomDetail;
import com.umc7.ZIC.reservation.validation.annotation.CheckReservationTime;
import com.umc7.ZIC.reservation.validation.annotation.CheckReservationTimeOverlap;
import com.umc7.ZIC.reservation.validation.validationSequence.ValidationStep;
import jakarta.validation.constraints.*;
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
            @NotBlank(message = "예약 번호는 필수 입력 항목입니다.")
            @Size(max = 15, message = "예약 번호는 최대 15자까지 입력 가능합니다.")
            String reservationNumber,

            @NotNull(message = "연습실 내부 방 id는 필수 입력 항목입니다.")
            @ExistPracticeRoomDetail(groups = ValidationStep.ExistValidation.class)
            @CheckPracticeRoomDetailStatus(groups = ValidationStep.StatusValidation.class)
            Long practiceRoomDetail,

            @NotNull(message = "예약 날짜는 필수 입력 항목입니다.")
            LocalDate date,

            @NotNull(message = "시작 시간은 필수 입력 항목입니다.")
            @DateTimeFormat(pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,

            @NotNull(message = "종료 시간은 필수 입력 항목입니다.")
            @DateTimeFormat(pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime endTime
    ) {}
}
