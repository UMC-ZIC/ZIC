package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.reservation.domain.Reservation;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ReservationQueryService {

    /**
     * 유저 Id와 page를 통해 연습실 예약 목록을 조회하는 Service
     * @param userId
     * @param localDate yyyy-MM-dd
     * @param page
     * @return
     */
    Page<Reservation> getReservationList(Long userId, LocalDate localDate, Integer page);
}
