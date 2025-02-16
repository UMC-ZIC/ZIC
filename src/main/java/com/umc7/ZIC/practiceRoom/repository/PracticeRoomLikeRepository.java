package com.umc7.ZIC.practiceRoom.repository;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomLike;
import com.umc7.ZIC.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PracticeRoomLikeRepository extends JpaRepository<PracticeRoomLike, Long> {

    // 좋아요 조회 (사용자와 연습실 ID로 조회)
    Optional<PracticeRoomLike> findByUserAndPracticeRoom(User user, PracticeRoom practiceRoom);

    // 좋아요 갯수 조회 (연습실 ID로 조회)
    @Query("SELECT COUNT(prl) FROM PracticeRoomLike prl WHERE prl.practiceRoom.id = :practiceRoomId")
    Long countByPracticeRoomId(@Param("practiceRoomId") Long practiceRoomId);

    // 좋아요 명단 조회
    @Query("SELECT pl.user.id FROM PracticeRoomLike pl WHERE pl.practiceRoom.id = :practiceRoomId")
    List<Long> findByPracticeRoomId(Long practiceRoomId);
}