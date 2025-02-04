package com.umc7.ZIC.practiceRoom.dto;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.domain.enums.RoomStatus;

import java.time.LocalDateTime;

public record PracticeRoomDetailResponseDto() {

    public record CreateResponseDto(
            Long practiceRoomDetailId,
            String name,
            LocalDateTime createdAt
    ) {
        public static CreateResponseDto from(PracticeRoomDetail practiceRoomDetail) {
            return new CreateResponseDto(
                    practiceRoomDetail.getId(),
                    practiceRoomDetail.getName(),
                    practiceRoomDetail.getCreatedAt()
            );
        }
    }

    public record GetResponseDto(
            Long practiceRoomDetailId,
            String name,
            String image,
            Integer fee,
            RoomStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static GetResponseDto from(PracticeRoomDetail practiceRoomDetail) {
            return new GetResponseDto(
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

    public record UpdateResponseDto(
            Long practiceRoomDetailId,
            String name,
            String image,
            Integer fee,
            RoomStatus status,
            LocalDateTime updatedAt
    ) {
        public static UpdateResponseDto from(PracticeRoomDetail practiceRoomDetail) {
            return new UpdateResponseDto(
                    practiceRoomDetail.getId(),
                    practiceRoomDetail.getName(),
                    practiceRoomDetail.getImage(),
                    practiceRoomDetail.getFee(),
                    practiceRoomDetail.getStatus(),
                    practiceRoomDetail.getUpdatedAt()
            );
        }
    }
}