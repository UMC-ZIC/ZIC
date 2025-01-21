package com.umc7.ZIC.practiceRoom.repository;
import static org.assertj.core.api.Assertions.assertThat;
import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.common.domain.enums.RegionType;
import com.umc7.ZIC.common.repository.RegionRepository;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;

import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@SpringBootTest
@Transactional
class PracticeRoomRepositoryTest {
    @Autowired
    private PracticeRoomRepository practiceRoomRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;

    private Region region;
    private User user;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 Region과 User 객체 생성 및 저장
        region = Region.builder()
                .name(RegionType.SEOUL)
                .build();
        user = User.builder()
                .name("테스트닉네임")
                .businessName("테스트 악기연습소")
                .businessNumber("전화번호")
                .email("12@12.com")
                .kakaoId(0L)
                .build();
        PracticeRoom practiceRoom1 = PracticeRoom.builder()
                .region(region)
                .user(user)
                .name("연습실 1")
                .address("주소 1")
                .latitude(37.5)
                .longitude(127.0)
                .build();
        PracticeRoom practiceRoom2 = PracticeRoom.builder()
                .region(region)
                .user(user)
                .name("연습실 2")
                .address("주소 2")
                .latitude(37.6)
                .longitude(127.1)
                .build();
        practiceRoomRepository.save(practiceRoom1);
        practiceRoomRepository.save(practiceRoom2);
        regionRepository.save(region);
        userRepository.save(user);
    }

    @Test
    @DisplayName("연습실 전체 조회 테스트")
    void findAllPracticeRoom() {
        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<PracticeRoom> practiceRooms = practiceRoomRepository.findAllPracticeRoom(pageable);

        // Then
        assertThat(practiceRooms.getContent())
                .hasSize(2)
                .extracting(PracticeRoom::getName)
                .containsExactly("연습실 1", "연습실 2");
    }



    @Test
    @DisplayName("연습실 ID로 조회 테스트")
    void findById() {
        // Given
        PracticeRoom practiceRoom = PracticeRoom.builder()
                .region(region)
                .user(user)
                .name("ZIC 연습실")
                .address("서울시 강남구")
                .latitude(37.5)
                .longitude(127.0)
                .build();
        PracticeRoom savedPracticeRoom = practiceRoomRepository.save(practiceRoom);

        // When
        Optional<PracticeRoom> foundPracticeRoom = practiceRoomRepository.findById(savedPracticeRoom.getId());

        // Then
        assertThat(foundPracticeRoom).isPresent();
        assertThat(foundPracticeRoom.get().getId()).isEqualTo(savedPracticeRoom.getId());
        assertThat(foundPracticeRoom.get().getName()).isEqualTo("ZIC 연습실");
    }
}
