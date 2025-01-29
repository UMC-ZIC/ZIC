package com.umc7.ZIC.reservation;

import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.Status;
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

    @Scheduled(fixedRate = 10000 * 6) // 1분마다 실행
    public void updatePendingReservations() {

        LocalDateTime thirtyMinutes = LocalDateTime.now().minusMinutes(3); // TODO : 시간 변경하기

        List<Reservation> reservationList = reservationCommandService.reservationToggleStatus(thirtyMinutes, Status.FAIL);

        System.out.println("------------------------------------------------------------------------------------------");
        reservationList.forEach(reservation -> System.out.println("{[Id : " + reservation.getId() +
                "] | [Status : " + reservation.getStatus() +
                "] | [StartTime : " + reservation.getStartTime() +
                "] | [EndTime : " + reservation.getEndTime() + "]}")); // TODO : 나중에 삭제하기
        System.out.println("------------------------------------------------------------------------------------------");
    }
}
