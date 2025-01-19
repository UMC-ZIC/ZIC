package com.umc7.ZIC.practiceRoom.dto;

import com.umc7.ZIC.common.domain.enums.RegionType;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;

import java.time.LocalDateTime;

public record PracticeRoomResponseDto() {

    public record CreateResponseDto(
            Long practiceRoomId,
            String name,
            String address,
            LocalDateTime createdAt

    ) {
        public static CreateResponseDto from(PracticeRoom practiceRoom) {
            return new CreateResponseDto(practiceRoom.getId(), practiceRoom.getName(), practiceRoom.getAddress(), practiceRoom.getCreatedAt());
        }
    }
    /**
     * 연습실 수정 결과 DTO
     */
    public record UpdateResponseDto(
            Long practiceRoomId,
            String name,
            String address,
            Double latitude, // 위도
            Double longitude, // 경도
            RegionType region, // 지역 (Region 엔티티의 regionName)
            LocalDateTime updatedAt
    ) {

        public static UpdateResponseDto from(PracticeRoom practiceRoom) {
            return new UpdateResponseDto(practiceRoom.getId(), practiceRoom.getName(), practiceRoom.getAddress(), practiceRoom.getLatitude(), practiceRoom.getLongitude(), practiceRoom.getRegion().getName(), practiceRoom.getUpdatedAt());
        }
    }

    /**
     * 연습실 단일 조회 결과 DTO
     */
    public record GetResponseDto(
            Long practiceRoomId, // 연습실 ID
            String name, // 연습실 이름
            String address, // 연습실 주소
            Double latitude, // 위도
            Double longitude, // 경도
            RegionType region, // 지역 (Region 엔티티의 regionName)
            LocalDateTime createdAt, // 생성 시간
            LocalDateTime updatedAt // 수정 시간
    ) {

        public static GetResponseDto from(PracticeRoom practiceRoom) {
            return new GetResponseDto(
                    practiceRoom.getId(),
                    practiceRoom.getName(),
                    practiceRoom.getAddress(),
                    practiceRoom.getLatitude(),
                    practiceRoom.getLongitude(),
                    practiceRoom.getRegion().getName(),
                    practiceRoom.getCreatedAt(),
                    practiceRoom.getUpdatedAt()
            );
        }
    }
}
