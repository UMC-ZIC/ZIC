package com.umc7.ZIC.reservation.repository;

import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {

    /**
     * 유저 Id, status, date를 통해 해당 유저의 특정 날짜의 연습실 예약 목록을 조회하는 Repository
     * @param user_id
     * @param date [연도, 월, 일]
     * @param pageRequest
     * @return
     */
    Page<Reservation> findAllByUserIdAndDateAndStatus(Long user_id, LocalDate date, Status status, PageRequest pageRequest);

    /**
     * 특정 status를 갖고 있고 생성된지 일정 시간이 지난 예약 목록을 조회하는 Repository
     * @param status
     * @param date
     * @return
     */
    List<Reservation> findByStatusAndCreatedAtBefore(Status status, LocalDateTime date);
}
