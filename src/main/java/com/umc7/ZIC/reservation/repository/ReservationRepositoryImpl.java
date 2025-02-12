package com.umc7.ZIC.reservation.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc7.ZIC.reservation.domain.QReservation;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.reservation.domain.enums.ReservationStatus;
import com.umc7.ZIC.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    QReservation qReservation = QReservation.reservation;

    @Override
    public Optional<List<Reservation>> findOverlappingReservations(Long practiceRoomDetailId, LocalDate date, LocalTime startTime, LocalTime endTime) {

        List<Reservation> overlappingReservations = jpaQueryFactory
                .selectFrom(qReservation)
                .where(
                        qReservation.practiceRoomDetail.id.eq(practiceRoomDetailId),
                        qReservation.status.eq(ReservationStatus.SUCCESS),
                        qReservation.date.eq(date),
                        qReservation.startTime.goe(startTime).and(qReservation.startTime.lt(endTime))
                                .or(qReservation.endTime.gt(startTime).and(qReservation.endTime.loe(endTime)))
                                .or(qReservation.startTime.loe(startTime).and(qReservation.endTime.goe(endTime)))
                )
                .fetch();

        return Optional.ofNullable(overlappingReservations.isEmpty() ? null : overlappingReservations);
    }

    @Override
    public List<UserResponseDto.UserMyPageDto.UserThisMonthPractice.UserThisMonthPracticeDetail> findUserThisMonthPracticeDetailsByUserId(Long userId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<UserResponseDto.UserMyPageDto.UserThisMonthPractice.UserThisMonthPracticeDetail> results = jpaQueryFactory
                .select(Projections.constructor(UserResponseDto.UserMyPageDto.UserThisMonthPractice.UserThisMonthPracticeDetail.class,
                        qReservation.practiceRoomDetail.practiceRoom.name,
                        qReservation.practiceRoomDetail.name,
                        qReservation.id.count().intValue()))
                .from(qReservation)
                .where(
                        qReservation.user.id.eq(userId),
                        qReservation.date.between(startOfMonth, endOfMonth)
                )
                .groupBy(qReservation.practiceRoomDetail.practiceRoom.name, qReservation.practiceRoomDetail.name)
                .fetch();
        return results;
    }

    @Override
    public List<UserResponseDto.UserMyPageDto.FrequentPracticeRooms.FrequentPracticeRoomDetail> findTop3PracticeRoomsByUserId(Long userId) {
        List<UserResponseDto.UserMyPageDto.FrequentPracticeRooms.FrequentPracticeRoomDetail> results = jpaQueryFactory
                .select(Projections.constructor(UserResponseDto.UserMyPageDto.FrequentPracticeRooms.FrequentPracticeRoomDetail.class,
                        qReservation.practiceRoomDetail.practiceRoom.name))
                .from(qReservation)
                .where(
                        qReservation.user.id.eq(userId)
                )
                .groupBy(qReservation.practiceRoomDetail.practiceRoom.name)
                .orderBy(qReservation.id.count().desc()) // 예약 횟수 기준으로 정렬
                .limit(3) // 상위 3개 선택
                .fetch();
        return results;
    }
}
