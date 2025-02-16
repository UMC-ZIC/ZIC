package com.umc7.ZIC.reservation.repository;

import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    Page<Reservation> findAllByUserIdAndDateAndStatus(Long user_id, LocalDate date, ReservationStatus status, PageRequest pageRequest);

    /**
     * 특정 status를 갖고 있고 생성된지 일정 시간이 지난 예약 목록을 조회하는 Repository
     * @param status
     * @param date
     * @return
     */
    List<Reservation> findByStatusAndCreatedAtBefore(ReservationStatus status, LocalDateTime date);


    /**
     * 유저 Id, status, date를 통해 해당 대여자의 특정 날짜의 연습실이 예약된 목록을 조회하는 Repository
     * @param userId
     * @param date [연도, 월, 일]
     * @param pageRequest
     * @return
     */
    @Query("SELECT r FROM Reservation r " +
            "JOIN r.practiceRoomDetail prd " +
            "JOIN prd.practiceRoom pr " +
            "JOIN pr.user u "+ // User 엔티티와 조인 추가
            "WHERE pr.user.id = :userId " +  // user ID 조건
            "AND r.date = :date " +          // 예약 날짜 조건
            "AND r.status = :status")         // 예약 상태 조건
    //user의 타입이 owner 이니까 파라미터는 일단 userId로 표기하였습니다
    Page<Reservation> findReservationsByOwnerIdAndDateAndStatus(
            @Param("userId") Long userId,
            @Param("date") LocalDate date,
            @Param("status") ReservationStatus status,
            PageRequest pageRequest);
}
