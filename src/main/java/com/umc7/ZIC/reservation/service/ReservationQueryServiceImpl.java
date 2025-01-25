package com.umc7.ZIC.reservation.service;

import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.Status;
import com.umc7.ZIC.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReservationQueryServiceImpl implements ReservationQueryService {
    private final ReservationRepository reservationRepository;

    @Override
    public Page<Reservation> getReservationList(Long userId, LocalDate localDate, Integer page) {
        return reservationRepository.findAllByUserIdAndDateAndStatus(userId, localDate, Status.SUCCESS,PageRequest.of(page, 10));
    }
}