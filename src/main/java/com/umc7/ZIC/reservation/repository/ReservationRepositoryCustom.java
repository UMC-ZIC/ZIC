package com.umc7.ZIC.reservation.repository;


import com.umc7.ZIC.reservation.domain.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryCustom {

    /**
     * 해당 예약과 겹치는 예약이 이미 DB에 존재하는지 조회하는 Repository
     * @param practiceRoomDetailId
     * @param date
     * @param startTime
     * @param endTime
     * @return
     */
    Optional<List<Reservation>> findOverlappingReservations(Long practiceRoomDetailId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
