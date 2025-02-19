package com.umc7.ZIC.practiceRoom.dto;

import com.umc7.ZIC.common.domain.enums.RegionType;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import lombok.Builder;


import java.time.LocalDateTime;

public record PracticeRoomResponseDto() {

    public record CreateResponseDto(
            Long practiceRoomId,
            String name,
            String address,
            String image,
            LocalDateTime createdAt

    ) {
        public static CreateResponseDto from(PracticeRoom practiceRoom) {
            return new CreateResponseDto(practiceRoom.getId(), practiceRoom.getName(), practiceRoom.getAddress(), practiceRoom.getImage(), practiceRoom.getCreatedAt());
        }
    }
    /**
     * 연습실 수정 결과 DTO
     */
    public record UpdateResponseDto(
            Long practiceRoomId,
            String name,
            String address,
            String image,
            Double latitude, // 위도
            Double longitude, // 경도
            RegionType region, // 지역 (Region 엔티티의 regionName)
            LocalDateTime updatedAt
    ) {

        public static UpdateResponseDto from(PracticeRoom practiceRoom) {
            return new UpdateResponseDto(practiceRoom.getId(), practiceRoom.getName(), practiceRoom.getAddress(), practiceRoom.getImage(), practiceRoom.getLatitude(), practiceRoom.getLongitude(), practiceRoom.getRegion().getName(), practiceRoom.getUpdatedAt());
        }
    }

    /**
     * 연습실 단일 조회 결과 DTO
     */
    @Builder
    public record GetResponseDto(
            Long practiceRoomId, // 연습실 ID
            String name, // 연습실 이름
            String address, // 연습실 주소
            String image, //연습실 image url
            Double latitude, // 위도
            Double longitude, // 경도
            String region, // 지역 (Region 엔티티의 regionName)
            LocalDateTime createdAt, // 생성 시간
            LocalDateTime updatedAt // 수정 시간

    ) {

        public static GetResponseDto from(PracticeRoom practiceRoom) {
            return new GetResponseDto(
                    practiceRoom.getId(),
                    practiceRoom.getName(),
                    practiceRoom.getAddress(),
                    practiceRoom.getImage(),
                    practiceRoom.getLatitude(),
                    practiceRoom.getLongitude(),
                    practiceRoom.getRegion().getName().getKoreanName(), // RegionType의 한글 이름 사용
                    practiceRoom.getCreatedAt(),
                    practiceRoom.getUpdatedAt()
            );
        }
    }
    @Builder
    public record GetListResponseDto(
            Long practiceRoomId, // 연습실 ID
            String name, // 연습실 이름
            String address, // 연습실 주소
            String image, //연습실 image url
            Double latitude, // 위도
            Double longitude, // 경도
            String region, // 지역 (Region 엔티티의 regionName)
            Integer totalRoomCount,     // 전체 방 개수
            Integer availableRoomCount,  // 예약 가능한 방 개수
            LocalDateTime createdAt, // 생성 시간
            LocalDateTime updatedAt // 수정 시간

    ) {

        public static GetListResponseDto from(PracticeRoom practiceRoom, int totalRoomCount, int availableRoomCount) {
            return GetListResponseDto.builder()
                    .practiceRoomId(practiceRoom.getId())
                    .name(practiceRoom.getName())
                    .address(practiceRoom.getAddress())
                    .image(practiceRoom.getImage())
                    .latitude(practiceRoom.getLatitude())
                    .longitude(practiceRoom.getLongitude())
                    .region(practiceRoom.getRegion().getName().getKoreanName()) // RegionType의 한글 이름 사용
                    .totalRoomCount(totalRoomCount)
                    .availableRoomCount(availableRoomCount)
                    .createdAt(practiceRoom.getCreatedAt())
                    .updatedAt(practiceRoom.getUpdatedAt())
                    .build();
        }
    }
}
