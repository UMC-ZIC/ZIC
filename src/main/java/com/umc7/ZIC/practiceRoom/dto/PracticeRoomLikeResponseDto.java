package com.umc7.ZIC.practiceRoom.dto;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoomLike;

import java.time.LocalDateTime;

public record PracticeRoomLikeResponseDto() {

    public record LikeResponseDto(
            Long likeId,
            Long practiceRoomId,
            Long userId,
            LocalDateTime createdAt
    ) {
        public static LikeResponseDto from(PracticeRoomLike practiceRoomLike) {
            return new LikeResponseDto(
                    practiceRoomLike.getId(),
                    practiceRoomLike.getPracticeRoom().getId(),
                    practiceRoomLike.getUser().getId(),
                    practiceRoomLike.getCreatedAt()
            );
        }
    }

    public record LikeCountResponseDto(
            Long count
    ) {
        public static LikeCountResponseDto from(Long count) {
            return new LikeCountResponseDto(count);
        }
    }
}