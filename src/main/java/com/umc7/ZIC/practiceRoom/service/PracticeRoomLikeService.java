package com.umc7.ZIC.practiceRoom.service;

import com.umc7.ZIC.practiceRoom.dto.PracticeRoomLikeResponseDto;

public interface PracticeRoomLikeService {
    PracticeRoomLikeResponseDto.LikeResponseDto createPracticeRoomLike(Long practiceRoomId, Long userId);
    PracticeRoomLikeResponseDto.LikeCountResponseDto getLikeCount(Long practiceRoomId);
}
