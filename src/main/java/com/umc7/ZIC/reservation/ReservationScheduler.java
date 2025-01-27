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

        List<Reservation> reservationList = reservationCommandService.reservationToggleStatus(thirtyMinutes, Status.PENDING);

        System.out.println(reservationList); // TODO : 나중에 삭제하기
    }
}
