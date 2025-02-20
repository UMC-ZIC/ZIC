package com.umc7.ZIC.practiceRoom.repository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.QPracticeRoomDetail;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.umc7.ZIC.practiceRoom.domain.QPracticeRoom.practiceRoom;
import static com.umc7.ZIC.practiceRoom.domain.QPracticeRoomDetail.practiceRoomDetail;
import static com.umc7.ZIC.practiceRoom.domain.QPracticeRoomInstrument.practiceRoomInstrument;
import static com.umc7.ZIC.reservation.domain.QReservation.reservation;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PracticeRoomRepositoryImpl implements PracticeRoomRepositoryCustom {

    private final JPAQueryFactory queryFactory;



    @Override
    public Page<PracticeRoom> findAvailablePracticeRoomsByRegionAndDateAndInstrument(
            Long regionId, LocalDate date, Long instrumentId, String priceSort, Pageable pageable) {

        // 정렬 조건 (OrderSpecifier)
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(priceSort);

        List<PracticeRoom> content = queryFactory
                .selectDistinct(practiceRoom)
                .from(practiceRoom)
                .leftJoin(practiceRoom.PracticeRoomDetailList, practiceRoomDetail)
                .leftJoin(practiceRoom.practiceRoomInstrumentList, practiceRoomInstrument)
                .where(
                        regionEq(regionId),
                        instrumentEq(instrumentId),
                        dateEq(date)
                )
                .orderBy(orderSpecifier) // 정렬 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(practiceRoom.countDistinct())
                .from(practiceRoom)
                .leftJoin(practiceRoom.PracticeRoomDetailList, practiceRoomDetail)
                .leftJoin(practiceRoom.practiceRoomInstrumentList, practiceRoomInstrument)
                .where(
                        regionEq(regionId),
                        instrumentEq(instrumentId),
                        dateEq(date)
                )
                .fetchFirst();

        long totalCount = total == null ? 0 : total;

        return new PageImpl<>(content, pageable, totalCount);
    }

    private BooleanExpression regionEq(Long regionId) {
        return regionId != null ? practiceRoom.region.id.eq(regionId) : null;
    }

    private BooleanExpression instrumentEq(Long instrumentId) {
        return instrumentId != null ? practiceRoomInstrument.instrument.id.eq(instrumentId) : null;
    }

    private BooleanExpression dateEq(LocalDate date) {
        if (date == null) {
            return null;
        }

        return null;
/*        return practiceRoom.PracticeRoomDetailList.any().status.eq(com.umc7.ZIC.practiceRoom.domain.enums.RoomStatus.AVAILABLE)
                .and(practiceRoom.PracticeRoomDetailList.any().id.notIn(
                        queryFactory.select(reservation.practiceRoomDetail.id)
                                .from(reservation)
                                .where(reservation.date.eq(date),
                                        reservation.status.eq(com.umc7.ZIC.reservation.domain.enums.ReservationStatus.SUCCESS))
                                .fetch()
                ));*/
    }

    private OrderSpecifier<?> getOrderSpecifier(String priceSort) {
        if (priceSort == null || priceSort.isEmpty()) {
            return new OrderSpecifier<>(Order.ASC, practiceRoom.id); // 기본 정렬 (ID 오름차순)
        }

        // 1. PathBuilder를 사용하여 동적으로 Path 생성
        PathBuilder<Object> practiceRoomDetailPath = new PathBuilder<>(Object.class, "practiceRoomDetail");

        // 2. numberTemplate을 사용하여 서브쿼리 생성.
        NumberExpression<Integer> minFee = Expressions.numberTemplate(
                Integer.class,
                "(SELECT min(practiceRoomDetail.fee) FROM PracticeRoomDetail practiceRoomDetail WHERE practiceRoomDetail.practiceRoom.id = {0})",
                practiceRoom.id
        );


        if ("asc".equalsIgnoreCase(priceSort)) {
            return minFee.asc().nullsLast();
        } else if ("desc".equalsIgnoreCase(priceSort)) {
            return minFee.desc().nullsLast();
        } else {
            return new OrderSpecifier<>(Order.ASC, practiceRoom.id);
        }
    }
}