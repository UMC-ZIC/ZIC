package com.umc7.ZIC.practiceRoom.repository;


import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PracticeRoomRepository extends JpaRepository<PracticeRoom, Long> {

    //연습실 리스트 조회
    @Query("SELECT pr FROM PracticeRoom pr")
    Page<PracticeRoom> findAllPracticeRoom(Pageable pageable);

    // 연습실 단일 조회
    @Query("SELECT pr FROM PracticeRoom pr WHERE pr.id = :id")
    Optional<PracticeRoom> findById(@Param("id") Long id);

    // 유저 id로 해당 유저가 등록한 연습실 검색
    Optional<PracticeRoom> findByUserId(Long userId);

    // 지역 날짜 필터링 적용
    @Query("SELECT DISTINCT pr FROM PracticeRoom pr " +
            "LEFT JOIN pr.PracticeRoomDetailList prd " +
            "WHERE (:regionId IS NULL OR pr.region.id = :regionId) " +
            "AND (:date IS NULL OR EXISTS (" +
            "    SELECT 1 FROM PracticeRoomDetail prd2 " +
            "    WHERE prd2.practiceRoom = pr " +
            "    AND prd2.status = 'AVAILABLE'" + // AVAILABLE 상태인 PracticeRoomDetail 확인
            "))")
    Page<PracticeRoom> findAvailablePracticeRoomsByRegionAndDate(
            @Param("regionId") Long regionId,
            @Param("date") LocalDate date,
            Pageable pageable);
}