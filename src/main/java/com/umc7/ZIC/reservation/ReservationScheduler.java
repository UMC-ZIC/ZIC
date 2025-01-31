package com.umc7.ZIC.reservation;

import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.ReservationStatus;
import com.umc7.ZIC.reservation.service.ReservationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {
    private final ReservationCommandService reservationCommandService;

    @Scheduled(fixedRate = 10000 * 6 * 5) // 5분마다 실행
    public void updatePendingReservations() {

        LocalDateTime thirtyMinutes = LocalDateTime.now().minusMinutes(30);

        reservationCommandService.reservationToggleStatus(thirtyMinutes, ReservationStatus.FAIL);

        /*
        // TODO : 주석처리
        List<Reservation> reservationList = reservationCommandService.reservationToggleStatus(thirtyMinutes, ReservationStatus.FAIL);
        System.out.println("------------------------------------------------------------------------------------------");
        reservationList.forEach(reservation -> System.out.println("{[Id : " + reservation.getId() +
                "] | [Status : " + reservation.getStatus() +
                "] | [StartTime : " + reservation.getStartTime() +
                "] | [EndTime : " + reservation.getEndTime() + "]}"));
                */
    }
}
