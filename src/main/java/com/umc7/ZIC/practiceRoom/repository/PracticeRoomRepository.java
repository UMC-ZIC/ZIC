package com.umc7.ZIC.practiceRoom.repository;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PracticeRoomRepository extends JpaRepository<PracticeRoom, Long> {

    //연습실 리스트 조회
    @Query("SELECT p FROM PracticeRoom p")
    Page<PracticeRoom> findAllPracticeRoom(Pageable pageable);

    // 연습실 단일 조회
    Optional<PracticeRoom> findById(Long id);
}
