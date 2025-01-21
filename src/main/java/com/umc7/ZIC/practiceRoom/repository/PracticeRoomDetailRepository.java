package com.umc7.ZIC.practiceRoom.repository;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PracticeRoomDetailRepository extends JpaRepository<PracticeRoomDetail, Long> {

    // 연습실 내부 연습방 리스트 조회 (특정 연습실 ID에 해당하는 모든 방 조회, 페이징)
    @Query("SELECT prd FROM PracticeRoomDetail prd WHERE prd.practiceRoom.id = :practiceRoomId")
    Page<PracticeRoomDetail> findAllByPracticeRoomId(@Param("practiceRoomId") Long practiceRoomId, Pageable pageable);

    // 연습실 내부 연습방 단일 조회 (방 ID로 조회)
    Optional<PracticeRoomDetail> findById(Long id);

    // 연습실 내부 연습방 단일 조회 (방 ID와 연습실로 조회)
    Optional<PracticeRoomDetail> findByIdAndPracticeRoom(Long id, PracticeRoom practiceRoom);

}