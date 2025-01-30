package com.umc7.ZIC.practiceRoom.controller;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.practiceRoom.dto.PageRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailResponseDto;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomDetailService;
import com.umc7.ZIC.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/practice-rooms/{practiceRoomId}")
@RequiredArgsConstructor
@Tag(name = "연습실 내부 방", description = "연습실 내부 방 CRUD")
public class PracticeRoomDetailController {

    private final PracticeRoomDetailService practiceRoomDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    //연습실 내부 방 등록
    @PostMapping("/rooms")
    @Operation(summary = "연습실 내부 방 등록 API", description = "연습실에 연습방을 등록할때 사용하는 API.")
    public ApiResponse<PracticeRoomDetailResponseDto.CreateResponseDto> createPracticeRoomDetail(
            @RequestBody @Valid PracticeRoomDetailRequestDto.CreateRequestDetailDto createRequest,
            @PathVariable Long practiceRoomId) {

        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        PracticeRoomDetailResponseDto.CreateResponseDto response = practiceRoomDetailService.createPracticeRoomDetail(createRequest, practiceRoomId, userId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 내부 방 목록 조회
    @GetMapping("/rooms")
    @Operation(summary = "연습실 내부 방 목록 조회 API", description = "연습실 내부 방 목록을 조회하는API.")
    public ApiResponse<Page<PracticeRoomDetailResponseDto.GetResponseDto>> getPracticeRoomDetailList(
            @ModelAttribute PageRequestDto request,
            @PathVariable Long practiceRoomId) {
        Page<PracticeRoomDetailResponseDto.GetResponseDto> response = practiceRoomDetailService.getPracticeRoomDetailList(request, practiceRoomId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 내부 방 단일 조회
    @GetMapping("/rooms/{roomId}")
    @Operation(summary = "연습실 내부 방 단일 조회 API", description = "연습실 내부 방을 단일 조회하는 API.")
    public ApiResponse<PracticeRoomDetailResponseDto.GetResponseDto> getPracticeRoomDetail(
            @PathVariable Long roomId,
            @PathVariable Long practiceRoomId) {
        PracticeRoomDetailResponseDto.GetResponseDto response = practiceRoomDetailService.getPracticeRoomDetail(roomId, practiceRoomId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 내부 방 정보 수정
    @PatchMapping("/rooms/{roomId}")
    @Operation(summary = "연습실 내부 방 정보 수정 API", description = "연습실 내부 방 정보를 수정하는 API.")
    public ApiResponse<PracticeRoomDetailResponseDto.UpdateResponseDto> updatePracticeRoomDetail(
            @RequestBody @Valid PracticeRoomDetailRequestDto.UpdateRequestDetailDto updateRequest,
            @PathVariable Long practiceRoomId,
            @PathVariable Long roomId) {
        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        PracticeRoomDetailResponseDto.UpdateResponseDto response = practiceRoomDetailService.updatePracticeRoomDetail(updateRequest, practiceRoomId, roomId, userId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 내부 방 삭제
    @DeleteMapping("/rooms/{roomId}")
    @Operation(summary = "연습실 내부 방 삭제 API", description = "연습실 내부 방을 삭제하는 API.")
    public ApiResponse<Void> deletePracticeRoomDetail(
            @PathVariable Long practiceRoomId,
            @PathVariable Long roomId) {
        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        practiceRoomDetailService.deletePracticeRoomDetail(practiceRoomId, roomId, userId);
        return ApiResponse.onSuccess(null);
    }
}