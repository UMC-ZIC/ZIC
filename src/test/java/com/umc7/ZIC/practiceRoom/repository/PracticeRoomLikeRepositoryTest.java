package com.umc7.ZIC.practiceRoom.repository;


import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.common.domain.enums.RegionType;
import com.umc7.ZIC.common.repository.RegionRepository;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomLike;
import com.umc7.ZIC.user.domain.User;

import com.umc7.ZIC.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PracticeRoomLikeRepositoryTest {

    @Autowired
    private PracticeRoomLikeRepository practiceRoomLikeRepository;
    @Autowired
    private PracticeRoomRepository practiceRoomRepository;
    @Autowired
    private UserRepository userRepository;//임시 임포트
    @Autowired
    private RegionRepository regionRepository; //임시


    private User user;
    private PracticeRoom practiceRoom;
    private Region region;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 Region, Owner, User, PracticeRoom 객체 생성 및 저장
        region = Region.builder()
                .name(RegionType.SEOUL)
                .build();
        regionRepository.save(region);

        user = User.builder()
                .email("test@example.com")
                .name("testUser")
                .kakaoId(0L)
                .region(region)
                .build();
        userRepository.save(user);


        practiceRoom = PracticeRoom.builder()
                .region(region)
                .user(user)
                .name("연습실 1")
                .address("주소 1")
                .latitude(37.5)
                .longitude(127.0)
                .build();
        practiceRoomRepository.save(practiceRoom);
    }

    @Test
    @DisplayName("사용자와 연습실 ID로 좋아요 조회 테스트")
    void findByUserAndPracticeRoom() {
        // Given
        PracticeRoomLike like = PracticeRoomLike.builder()
                .user(user)
                .practiceRoom(practiceRoom)
                .build();
        practiceRoomLikeRepository.save(like);

        // When
        Optional<PracticeRoomLike> foundLike = practiceRoomLikeRepository.findByUserAndPracticeRoom(user, practiceRoom);

        // Then
        assertThat(foundLike).isPresent();
        assertThat(foundLike.get().getUser()).isEqualTo(user);
        assertThat(foundLike.get().getPracticeRoom()).isEqualTo(practiceRoom);
    }

    @Test
    @DisplayName("연습실 ID로 좋아요 개수 조회 테스트")
    void countByPracticeRoomId() {
        // Given
        PracticeRoomLike like1 = PracticeRoomLike.builder()
                .user(user)
                .practiceRoom(practiceRoom)
                .build();
        practiceRoomLikeRepository.save(like1);

        User anotherUser = User.builder()
                .email("another@example.com")
                .name("anotherUser")
                .kakaoId(10000L)
                .region(region)
                .build();
        userRepository.save(anotherUser);

        PracticeRoomLike like2 = PracticeRoomLike.builder()
                .user(anotherUser)
                .practiceRoom(practiceRoom)
                .build();
        practiceRoomLikeRepository.save(like2);

        // When
        Long count = practiceRoomLikeRepository.countByPracticeRoomId(practiceRoom.getId());

        // Then
        assertThat(count).isEqualTo(2); //테스트 DB로 구현해야함
    }
}