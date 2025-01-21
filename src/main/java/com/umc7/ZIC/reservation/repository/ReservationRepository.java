package com.umc7.ZIC.reservation.repository;

import com.umc7.ZIC.reservation.domain.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * 유저 Id와 date를 통해 해당 유저의 특정 날짝의 연습실 예약 목록을 조회하는 Repository
     * @param user_id
     * @param date [연도, 월, 일]
     * @param pageRequest
     * @return
     */
    Page<Reservation> findAllByUserIdAndDate(Long user_id, LocalDate date, PageRequest pageRequest);
}
