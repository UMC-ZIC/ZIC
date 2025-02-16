package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.dto.ReservationResponseDTO;
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

    /**
     * 유저 Id가 Owner 타입인 대여자가 등록한 연습실을 page를 통해 예약된 목록을 조회하는 Service
     * @param userId
     * @param localDate yyyy-MM-dd
     * @param page
     * @return
     */
    //user 객체의 type이 owner인 경우니 파라미터는 일단 userId로 표기하였습니다.
    Page<Reservation> getOwnerReservationList(Long userId, LocalDate localDate, Integer page);

    /**
     * userId와 date를 입력 받아 해당 월에 예약한 날짜를 조회하는 Service
     * @param userId
     * @param role
     * @param date
     * @return
     */
    ReservationResponseDTO.ReservationMonthUserDTO getUserReservationMonth(Long userId, String role, LocalDate date);

    /**
     * 해당 대여자가 등록한 연습실에 예약된 특정 달 동안의 예약 날짜 조회 Service
     * @param userId
     * @param role
     * @param date
     * @return
     */
    ReservationResponseDTO.ReservationMonthOwnerDTO getOwnerReservationMonth(Long userId, String role, LocalDate date);
}
