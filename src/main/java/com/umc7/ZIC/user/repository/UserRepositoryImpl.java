package com.umc7.ZIC.user.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc7.ZIC.practiceRoom.domain.QPracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.QPracticeRoomDetail;
import com.umc7.ZIC.reservation.domain.QReservation;
import com.umc7.ZIC.reservation.domain.QReservationDetail;
import com.umc7.ZIC.reservation.domain.enums.Status;
import com.umc7.ZIC.user.domain.QUser;
import com.umc7.ZIC.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final QUser user = QUser.user;
    private final JPAQueryFactory jpaQueryFactory;

    private final QPracticeRoom qPracticeRoom = QPracticeRoom.practiceRoom;
    private final QPracticeRoomDetail qPracticeRoomDetail = QPracticeRoomDetail.practiceRoomDetail;
    private final QReservation qReservation = QReservation.reservation;
    private final QReservationDetail qReservationDetail = QReservationDetail.reservationDetail;

    @Override
    public UserResponseDto.user findUserByEmail(String email) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(user.email.eq(email));

        return jpaQueryFactory
                .select(Projections.constructor(UserResponseDto.user.class,
                        user.id,
                        user.email,
                        user.name,
                        user.kakaoId,
                        user.region))
                .from(user)
                .where(predicate)
                .fetchOne();
    }

    @Override
    public List<UserResponseDto.OwnerEarning> findOwnerEarningByUserIdAndMonth(Long userId, LocalDate targetMonth) {
        // targetMonth의 첫 날과 마지막 날을 구한다.
        LocalDate startOfMonth = targetMonth.withDayOfMonth(1); // 해당 월의 첫 날
        LocalDate endOfMonth = targetMonth.withDayOfMonth(targetMonth.lengthOfMonth());

        List<Tuple> results = jpaQueryFactory.select(
                qPracticeRoom.user.id,
                qPracticeRoom.id,
                qPracticeRoom.name,
                qPracticeRoomDetail.id,
                qPracticeRoomDetail.name,
                qPracticeRoomDetail.fee,
                qReservation.count(),
                qReservationDetail.amount.sum()
        )
                .from(qPracticeRoom)
                .join(qPracticeRoom.PracticeRoomDetailList, qPracticeRoomDetail)
                .leftJoin(qPracticeRoomDetail.reservationList, qReservation)
                .on(qReservation.date.between(startOfMonth, endOfMonth).and(qReservation.status.eq(Status.SUCCESS)))
                .leftJoin(qReservation.reservationDetail, qReservationDetail)
                .where(qPracticeRoom.user.id.eq(userId))
                .groupBy(
                        qPracticeRoom.user.id,
                        qPracticeRoom.id,
                        qPracticeRoom.name,
                        qPracticeRoomDetail.id,
                        qPracticeRoomDetail.name,
                        qPracticeRoomDetail.fee
                )
                .fetch();

        return results.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(qPracticeRoom.id),
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                groupedTuples -> {
                                    Tuple first = groupedTuples.get(0);
                                    List<UserResponseDto.OwnerEarning.PracticeRoomDetailStat> practiceRoomDetailStatList = groupedTuples.stream()
                                            .map(tuple -> UserResponseDto.OwnerEarning.PracticeRoomDetailStat.builder()
                                                    .practiceRoomDetailId(tuple.get(qPracticeRoomDetail.id))
                                                    .practiceRoomDetailName(tuple.get(qPracticeRoomDetail.name))
                                                    .fee(tuple.get(qPracticeRoomDetail.fee))
                                                    .reservationCount(tuple.get(qReservation.count()))
                                                    .totalRevenue(tuple.get(qReservationDetail.amount.sum()) == null ? 0 : tuple.get(qReservationDetail.amount.sum()))
                                                    .build())
                                            .sorted(Comparator.comparingLong(UserResponseDto.OwnerEarning.PracticeRoomDetailStat::practiceRoomDetailId))  // PracticeRoomDetailId 순으로 정렬
                                            .collect(Collectors.toList());

                                    return UserResponseDto.OwnerEarning.builder()
                                            .userId(first.get(qPracticeRoom.user.id))
                                            .practiceRoomId(first.get(qPracticeRoom.id))
                                            .practiceRoomName(first.get(qPracticeRoom.name))
                                            .practiceRoomDetail(practiceRoomDetailStatList)
                                            .build();
                                }
                        )
                ))
                .values()
                .stream()
                .toList();
    }

    @Override
    public List<UserResponseDto.OwnerMonthlyEarning> findOwnerMonthlyEarningByUserId(Long userId) {
        // 현재 날짜를 기준으로 12개월 전 시작 날짜와 이번 달 계산
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(11).withDayOfMonth(1); // 12개월 전의 첫 날
        LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth()); // 이번 달의 마지막 날

        // 12개월의 월별 시작 날짜 계산 (1월 -> 12월, 2월 -> 1월, ... 순으로 조회)
        List<LocalDate> monthRange = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            monthRange.add(startDate.plusMonths(i));
        }

        // 예약 데이터 조회
        List<Tuple> results = jpaQueryFactory.select(
                        qPracticeRoom.user.id,
                        qReservation.date.year(),
                        qReservation.date.month(),
                        qReservationDetail.amount.sum()
                )
                .from(qPracticeRoom)
                .join(qPracticeRoom.PracticeRoomDetailList, qPracticeRoomDetail)
                .leftJoin(qPracticeRoomDetail.reservationList, qReservation)
                .on(qReservation.status.eq(Status.SUCCESS))
                .leftJoin(qReservation.reservationDetail, qReservationDetail)
                .where(qPracticeRoom.user.id.eq(userId)
                        .and(qReservation.date.between(startDate, endDate))) // 지난 12개월 데이터를 조회
                .groupBy(qPracticeRoom.id, qPracticeRoomDetail.id, qReservation.date.year(), qReservation.date.month()) // 연도와 월별 그룹화
                .fetch();

        // 월별 수익 계산 (Map으로 누적 수익 계산)
        Map<String, Integer> monthlyRevenueMap = new HashMap<>();
        for (Tuple tuple : results) {
            String key = tuple.get(qReservation.date.year()) + "-" + tuple.get(qReservation.date.month());
            Integer totalRevenue = tuple.get(qReservationDetail.amount.sum());
            monthlyRevenueMap.merge(key, totalRevenue, Integer::sum);
        }

        // 월별 수익 리스트 생성 (12개월 기준으로 월별 누적 수익 계산)
        List<UserResponseDto.OwnerMonthlyEarning> monthlyEarnings = monthRange.stream()
                .map(date -> {
                    String key = date.getYear() + "-" + date.getMonthValue();
                    Integer totalRevenue = monthlyRevenueMap.getOrDefault(key, 0); // 수익이 없으면 0으로 설정
                    return UserResponseDto.OwnerMonthlyEarning.builder()
                            .year(date.getYear())
                            .month(date.getMonthValue())
                            .totalRevenue(totalRevenue)
                            .build();
                })
                .collect(Collectors.toList());

        return monthlyEarnings;
    }
}
