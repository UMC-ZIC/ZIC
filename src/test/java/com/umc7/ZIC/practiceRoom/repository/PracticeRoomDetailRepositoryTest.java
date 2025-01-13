package com.umc7.ZIC.practiceRoom.repository;

import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.common.domain.enums.RegionType;
import com.umc7.ZIC.owner.domain.Owner;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.repository.temp.OwnerRepository;
import com.umc7.ZIC.practiceRoom.repository.temp.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class PracticeRoomDetailRepositoryTest {

    @Autowired
    private PracticeRoomDetailRepository practiceRoomDetailRepository;

    @Autowired
    private PracticeRoomRepository practiceRoomRepository;

    @Autowired
    private RegionRepository regionRepository; //임시

    @Autowired
    private OwnerRepository ownerRepository; //임시

    private PracticeRoom savedPracticeRoom1;
    private PracticeRoom savedPracticeRoom2;
    private Region region;
    private Owner owner;

    @BeforeEach
    void setUp() {
        // 테스트를 위한 Region과 Owner 객체 생성 및 저장
        region = Region.builder()
                .name(RegionType.SEOUL)
                .build();
        owner = Owner.builder()
                .name("테스트닉네임")
                .businessName("테스트 악기연습소")
                .businessNumber("전화번호")
                .email("12@12.com")
                .kakaoId("test")
                .build();
        PracticeRoom practiceRoom1 = PracticeRoom.builder()
                .region(region)
                .owner(owner)
                .name("연습실 1")
                .address("주소 1")
                .latitude(37.5)
                .longitude(127.0)
                .build();
        PracticeRoom practiceRoom2 = PracticeRoom.builder()
                .region(region)
                .owner(owner)
                .name("연습실 2")
                .address("주소 2")
                .latitude(37.6)
                .longitude(127.1)
                .build();
        savedPracticeRoom1 =practiceRoomRepository.save(practiceRoom1);
        savedPracticeRoom2 =practiceRoomRepository.save(practiceRoom2);
        regionRepository.save(region);
        ownerRepository.save(owner);
    }

    @Test
    @DisplayName("연습실 ID로 내부 방 리스트 조회 테스트")
    void findAllByPracticeRoomId() {
        // Given
        PracticeRoomDetail detail1 = PracticeRoomDetail.builder()
                .practiceRoom(savedPracticeRoom1)
                .name("연습실 A")
                .image("image_url_a")
                .fee(10000)
                .build();
        PracticeRoomDetail detail2 = PracticeRoomDetail.builder()
                .practiceRoom(savedPracticeRoom1)
                .name("연습실 B")
                .image("image_url_b")
                .fee(12000)
                .build();
        practiceRoomDetailRepository.save(detail1);
        practiceRoomDetailRepository.save(detail2);

        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<PracticeRoomDetail> details = practiceRoomDetailRepository.findAllByPracticeRoomId(savedPracticeRoom1.getId(), pageable);

        // Then
        assertThat(details.getContent())
                .hasSize(2)
                .extracting(PracticeRoomDetail::getName)
                .containsExactly("연습실 A", "연습실 B");
    }
    @Test
    @DisplayName("연습실 내부 방 ID로 조회 테스트")
    void findById() {
        // Given
        PracticeRoomDetail practiceRoomDetail = PracticeRoomDetail.builder()
                .practiceRoom(savedPracticeRoom1)
                .name("연습실 A")
                .image("image_url")
                .fee(10000)
                .build();
        PracticeRoomDetail savedDetail = practiceRoomDetailRepository.save(practiceRoomDetail);

        // When:
        Optional<PracticeRoomDetail> foundRoomDetail = practiceRoomDetailRepository.findById(savedDetail.getId());

        // Then: 조회된 PracticeRoomDetail 검증
        assertThat(foundRoomDetail).isPresent();
        assertThat(foundRoomDetail.get().getId()).isEqualTo(savedDetail.getId());
        assertThat(foundRoomDetail.get().getName()).isEqualTo("연습실 A");
        assertThat(foundRoomDetail.get().getPracticeRoom()).isEqualTo(savedPracticeRoom1);
    }
}