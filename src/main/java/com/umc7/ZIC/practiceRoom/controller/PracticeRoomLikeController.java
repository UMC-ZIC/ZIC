package com.umc7.ZIC.practiceRoom.controller;

import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomLikeResponseDto;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/practice-rooms")
@RequiredArgsConstructor
@Tag(name = "연습실 좋아요", description = "연습실 좋아요 관련 API")
public class PracticeRoomLikeController {

    private final PracticeRoomLikeService practiceRoomLikeService;

    @PostMapping("/{practiceRoomId}/like")
    @Operation(summary = "연습실 좋아요/취소 API", description = "사용자가 연습실에 좋아요를 누르거나 취소할 때 사용하는 (두번 누르면 취소)API")
    public ApiResponse<PracticeRoomLikeResponseDto.LikeResponseDto> toggleLikePracticeRoom(
            @PathVariable Long practiceRoomId,
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        PracticeRoomLikeResponseDto.LikeResponseDto response = practiceRoomLikeService.createPracticeRoomLike(practiceRoomId, userId);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/{practiceRoomId}/likes/count")
    @Operation(summary = "연습실 좋아요 개수 조회 API", description = "연습실의 좋아요 개수를 조회할 때 사용하는 API")
    public ApiResponse<PracticeRoomLikeResponseDto.LikeCountResponseDto> getLikeCount(
            @PathVariable Long practiceRoomId) {
        PracticeRoomLikeResponseDto.LikeCountResponseDto response = practiceRoomLikeService.getLikeCount(practiceRoomId);
        return ApiResponse.onSuccess(response);
    }
}