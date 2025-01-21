package com.umc7.ZIC.practiceRoom.controller;

import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.practiceRoom.dto.PageRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailResponseDto;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomDetailService;
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

    //연습실 내부 방 등록
    @PostMapping("/rooms")
    @Operation(summary = "연습실 내부 방 등록 API", description = "연습실에 연습방을 등록할때 사용하는 API.")
    public ApiResponse<PracticeRoomDetailResponseDto.CreateResponseDto> createPracticeRoomDetail(
            @RequestBody @Valid PracticeRoomDetailRequestDto.CreateRequestDetailDto createRequest,
            @PathVariable Long practiceRoomId,
            @RequestParam Long userId) {
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
    @GetMapping("/rooms/{room_id}")
    @Operation(summary = "연습실 내부 방 단일 조회 API", description = "연습실 내부 방을 단일 조회하는 API.")
    public ApiResponse<PracticeRoomDetailResponseDto.GetResponseDto> getPracticeRoomDetail(
            @PathVariable Long room_id,
            @PathVariable Long practiceRoomId) {
        PracticeRoomDetailResponseDto.GetResponseDto response = practiceRoomDetailService.getPracticeRoomDetail(room_id, practiceRoomId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 내부 방 정보 수정
    @PatchMapping("/rooms/{room_id}")
    @Operation(summary = "연습실 내부 방 정보 수정 API", description = "연습실 내부 방 정보를 수정하는 API.")
    public ApiResponse<PracticeRoomDetailResponseDto.UpdateResponseDto> updatePracticeRoomDetail(
            @RequestBody @Valid PracticeRoomDetailRequestDto.UpdateRequestDetailDto updateRequest,
            @PathVariable Long practiceRoomId,
            @PathVariable Long room_id) {
        PracticeRoomDetailResponseDto.UpdateResponseDto response = practiceRoomDetailService.updatePracticeRoomDetail(updateRequest, practiceRoomId, room_id);
        return ApiResponse.onSuccess(response);
    }

    //연습실 내부 방 삭제
    @DeleteMapping("/rooms/{room_id}")
    @Operation(summary = "연습실 내부 방 삭제 API", description = "연습실 내부 방을 삭제하는 API.")
    public ApiResponse<Void> deletePracticeRoomDetail(
            @PathVariable Long practiceRoomId,
            @PathVariable Long room_id) {
        practiceRoomDetailService.deletePracticeRoomDetail(practiceRoomId, room_id);
        return ApiResponse.onSuccess(null);
    }
}