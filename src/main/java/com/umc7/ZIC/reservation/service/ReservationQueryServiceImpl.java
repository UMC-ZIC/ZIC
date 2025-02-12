package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.ReservationStatus;
import com.umc7.ZIC.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationQueryServiceImpl implements ReservationQueryService {
    private final ReservationRepository reservationRepository;

    @Override
    public Page<Reservation> getReservationList(Long userId, LocalDate localDate, Integer page) {
        return reservationRepository.findAllByUserIdAndDateAndStatus(userId, localDate, ReservationStatus.SUCCESS,PageRequest.of(page, 10));
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public Optional<List<Reservation>> overlappingReservation(Long practiceRoomDetailId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return reservationRepository.findOverlappingReservations(practiceRoomDetailId, date, startTime, endTime);
    }

    @Override
    public Page<Reservation> getOwnerReservationList(Long userId, LocalDate localDate, Integer page) {
        return reservationRepository.findReservationsByOwnerIdAndDateAndStatus(userId, localDate, ReservationStatus.SUCCESS,PageRequest.of(page, 10));
    }
}