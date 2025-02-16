package com.umc7.ZIC.reservation.repository;


import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.user.dto.UserResponseDto;

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


    List<UserResponseDto.UserMyPageDto.UserThisMonthPractice.UserThisMonthPracticeDetail> findUserThisMonthPracticeDetailsByUserId(Long userId);
    List<UserResponseDto.UserMyPageDto.FrequentPracticeRooms.FrequentPracticeRoomDetail> findTop3PracticeRoomsByUserId(Long userId);

    /**
     * 특정 달에 예약한 모든 예약 날짜를 조회하는 Repository
     * @param userId
     * @param date
     * @return
     */
    List<LocalDate> findReservationDateByUserIdAndMonth(Long userId, LocalDate date);

    /**
     * 해당 대여자가 등록한 연습실에 예약된 특정 달 동안의 예약 날짜 조회 Repository
     * @param userId
     * @param date
     * @return
     */
    List<LocalDate> findReservationDateByOwnerIdAndMonth(Long userId, LocalDate date);
}
