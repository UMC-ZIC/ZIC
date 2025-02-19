package com.umc7.ZIC.practiceRoom.dto;

import com.umc7.ZIC.common.domain.enums.RegionType;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import lombok.Builder;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            List<String> instrumentNames, // 악기 이름 목록 추가
            Integer minFee, // 최저 요금 필드 추가
            LocalDateTime createdAt, // 생성 시간
            LocalDateTime updatedAt // 수정 시간

    ) {

        public static GetListResponseDto from(PracticeRoom practiceRoom, int totalRoomCount, int availableRoomCount) {
            // PracticeRoomInstrument를 통해 악기 이름 목록 가져오기 (null 처리)
            List<String> instrumentNames = practiceRoom.getPracticeRoomInstrumentList() == null ?
                    Collections.emptyList() : // practiceRoomInstrumentList가 null이면 빈 리스트 반환
                    practiceRoom.getPracticeRoomInstrumentList().stream()
                            .map(practiceRoomInstrument -> {
                                if (practiceRoomInstrument == null || practiceRoomInstrument.getInstrument() == null) {
                                    return null; // null이면 null 반환 ( 걸러냄)
                                }
                                return practiceRoomInstrument.getInstrument().getName().getKoreanName(); // InstrumentType의 한글 이름
                            })
                            .filter(Objects::nonNull) // null 값 제거
                            .collect(Collectors.toList());
            // 최저 요금 계산
            Integer minFee = practiceRoom.getPracticeRoomDetailList() == null ? null :
                    practiceRoom.getPracticeRoomDetailList().stream()
                            .filter(Objects::nonNull) // null인 PracticeRoomDetail 제외
                            .map(PracticeRoomDetail::getFee)
                            .filter(Objects::nonNull)  // fee가 null인 경우 제외
                            .min(Integer::compareTo) // Optional<Integer> 반환
                            .orElse(null); // 최저 요금이 없으면 null

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
                    .instrumentNames(instrumentNames) // 악기 이름 목록 추가
                    .createdAt(practiceRoom.getCreatedAt())
                    .updatedAt(practiceRoom.getUpdatedAt())
                    .minFee(minFee) // 최저 요금 추가
                    .build();
        }
    }
}
