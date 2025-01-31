package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.reservation.domain.Reservation;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationQueryService {

    /**
     * 유저 Id와 page를 통해 연습실 예약 목록을 조회하는 Service
     * @param userId
     * @param localDate yyyy-MM-dd
     * @param page
     * @return
     */
    Page<Reservation> getReservationList(Long userId, LocalDate localDate, Integer page);

    /**
     * 해당 id를 통해 예약 데이터를 조회하는 Service
     * @param id
     * @return
     */
    Optional<Reservation> getReservationById(Long id);

    /**
     * 요청한 예약이 DB에 존재하는 예약과 시간이 겹치는지 조회하는 Service
     * @param practiceRoomDetailId
     * @param date
     * @param startTime
     * @param endTime
     * @return
     */
    Optional<List<Reservation>> overlappingReservation(Long practiceRoomDetailId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
