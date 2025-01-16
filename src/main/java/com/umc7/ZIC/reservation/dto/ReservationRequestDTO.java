package com.umc7.ZIC.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationRequestDTO {

    /**
     * 연습실 예약 시 클라이언트에서 정보를 받아오는 DTO
     * @param reservationNumber
     * @param practiceRoomDetail
     * @param user
     * @param date
     * @param startTime
     * @param endTime
     */
    @Builder
    public record reservationRegistDTO(
            String reservationNumber,
            Long practiceRoomDetail,
            Long user,
            LocalDate date,
            @DateTimeFormat(pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,
            @DateTimeFormat(pattern = "HH:mm")
            @JsonFormat(pattern = "HH:mm")
            LocalTime endTime,
            int price
    ) {}
}
