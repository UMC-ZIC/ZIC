package com.umc7.ZIC.reservation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc7.ZIC.reservation.domain.QReservation;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QReservation qReservation = QReservation.reservation;

    @Override
    public Optional<List<Reservation>> findOverlappingReservations(Long practiceRoomDetailId, LocalDate date, LocalTime startTime, LocalTime endTime) {

        List<Reservation> overlappingReservations = jpaQueryFactory
                .selectFrom(qReservation)
                .where(
                        qReservation.practiceRoomDetail.id.eq(practiceRoomDetailId),
                        qReservation.status.eq(Status.SUCCESS),
                        qReservation.date.eq(date),
                        qReservation.startTime.goe(startTime).and(qReservation.startTime.lt(endTime))
                                .or(qReservation.endTime.gt(startTime).and(qReservation.endTime.loe(endTime)))
                                .or(qReservation.startTime.loe(startTime).and(qReservation.endTime.goe(endTime)))
                )
                .fetch();

        return Optional.ofNullable(overlappingReservations.isEmpty() ? null : overlappingReservations);
    }
}
