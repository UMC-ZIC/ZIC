package com.umc7.ZIC.practiceRoom.dto;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.domain.enums.RoomStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record PracticeRoomDetailResponseDto() {

    public record CreateDetailResponseDto(
            Long practiceRoomDetailId,
            String name,
            Integer fee,
            LocalDateTime createdAt
    ) {
        public static CreateDetailResponseDto from(PracticeRoomDetail practiceRoomDetail) {
            return new CreateDetailResponseDto(
                    practiceRoomDetail.getId(),
                    practiceRoomDetail.getName(),
                    practiceRoomDetail.getFee(),
                    practiceRoomDetail.getCreatedAt()
            );
        }
    }

    public record GetDetailResponseDto(
            Long practiceRoomDetailId,
            String name,
            String image,
            Integer fee,
            RoomStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static GetDetailResponseDto from(PracticeRoomDetail practiceRoomDetail) {
            return new GetDetailResponseDto(
                    practiceRoomDetail.getId(),
                    practiceRoomDetail.getName(),
                    practiceRoomDetail.getImage(),
                    practiceRoomDetail.getFee(),
                    practiceRoomDetail.getStatus(),
                    practiceRoomDetail.getCreatedAt(),
                    practiceRoomDetail.getUpdatedAt()
            );
        }
    }

    public record UpdateDetailResponseDto(
            Long practiceRoomDetailId,
            String name,
            String image,
            Integer fee,
            RoomStatus status,
            LocalDateTime updatedAt
    ) {
        public static UpdateDetailResponseDto from(PracticeRoomDetail practiceRoomDetail) {
            return new UpdateDetailResponseDto(
                    practiceRoomDetail.getId(),
                    practiceRoomDetail.getName(),
                    practiceRoomDetail.getImage(),
                    practiceRoomDetail.getFee(),
                    practiceRoomDetail.getStatus(),
                    practiceRoomDetail.getUpdatedAt()
            );
        }
    }

    @Builder
    public record GetOwnerDetailResponseDto(
            PracticeRoomDTO practiceRoomDTO,
            List<PracticeRoomDetailDTO> practiceRoomDetailDTO
    ) {
        @Builder
        public record PracticeRoomDTO (
                Long practiceRoomId,
                String img,
                String region,
                String address,
                Double longitude,
                Double latitude
        ) {}

        @Builder
        public record PracticeRoomDetailDTO (
                Long practiceRoomDetailId,
                String img,
                String name,
                Integer fee,
                RoomStatus status
        ) {}
    }
}