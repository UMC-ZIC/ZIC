package com.umc7.ZIC.practiceRoom.controller;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.practiceRoom.dto.AvailableTimeSlot;
import com.umc7.ZIC.practiceRoom.dto.PageRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailResponseDto;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomDetailService;
import com.umc7.ZIC.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/practice-room-details")
@RequiredArgsConstructor
@Tag(name = "연습실 내부 방", description = "연습실 내부 방 CRUD")
public class PracticeRoomDetailController {

    private final PracticeRoomDetailService practiceRoomDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    //연습실 내부 방 등록
    @PostMapping
    @Operation(summary = "연습실 내부 방 등록 API", description = "연습실에 연습방을 등록할때 사용하는 API.")
    public ApiResponse<PracticeRoomDetailResponseDto.CreateDetailResponseDto> createPracticeRoomDetail(
            @RequestBody @Valid PracticeRoomDetailRequestDto.CreateRequestDetailDto createRequest,
            @RequestParam("practiceRoomId") Long practiceRoomId) {

        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        PracticeRoomDetailResponseDto.CreateDetailResponseDto response = practiceRoomDetailService.createPracticeRoomDetail(createRequest, practiceRoomId, userId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 내부 방 목록 조회
    @GetMapping
    @Operation(summary = "연습실 내부 방 목록 조회 API", description = "연습실 내부 방 목록을 조회하는API.")
    public ApiResponse<Page<PracticeRoomDetailResponseDto.GetDetailResponseDto>> getPracticeRoomDetailList(
            @ModelAttribute PageRequestDto request,
            @RequestParam("practiceRoomId") Long practiceRoomId) {
        Page<PracticeRoomDetailResponseDto.GetDetailResponseDto> response = practiceRoomDetailService.getPracticeRoomDetailList(request, practiceRoomId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 내부 방 단일 조회
    @GetMapping("/{practiceRoomDetailId}")
    @Operation(summary = "연습실 내부 방 단일 조회 API", description = "연습실 내부 방을 단일 조회하는 API.")
    public ApiResponse<PracticeRoomDetailResponseDto.GetDetailResponseDto> getPracticeRoomDetail(
            @PathVariable Long practiceRoomDetailId) {
        PracticeRoomDetailResponseDto.GetDetailResponseDto response = practiceRoomDetailService.getPracticeRoomDetail(practiceRoomDetailId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 내부 방 정보 수정
    @PatchMapping("/{practiceRoomDetailId}")
    @Operation(summary = "연습실 내부 방 정보 수정 API", description = "연습실 내부 방 정보를 수정하는 API.")
    public ApiResponse<PracticeRoomDetailResponseDto.UpdateDetailResponseDto> updatePracticeRoomDetail(
            @RequestBody @Valid PracticeRoomDetailRequestDto.UpdateRequestDetailDto updateRequest,
            @PathVariable Long practiceRoomDetailId) {
        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        PracticeRoomDetailResponseDto.UpdateDetailResponseDto response = practiceRoomDetailService.updatePracticeRoomDetail(updateRequest, practiceRoomDetailId, userId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 내부 방 삭제
    @DeleteMapping("/{practiceRoomDetailId}")
    @Operation(summary = "연습실 내부 방 삭제 API", description = "연습실 내부 방을 삭제하는 API.")
    public ApiResponse<Void> deletePracticeRoomDetail(
            @PathVariable Long practiceRoomDetailId) {
        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        practiceRoomDetailService.deletePracticeRoomDetail(practiceRoomDetailId, userId);
        return ApiResponse.onSuccess(null);
    }

    @Operation(summary = "연습실 내부 방 예약 가능 시간 조회 API", description = "연습실 내부 방의 특정 날짜에 대한 예약 가능 시간을 조회합니다.")
    @Parameter(name = "practiceRoomDetailId", description = "연습실 내부 방의 ID", in = ParameterIn.PATH, required = true)
    @Parameter(name = "date", description = "조회할 날짜 (yyyy-MM-dd)", in = ParameterIn.QUERY, required = true)
    @GetMapping("/{practiceRoomDetailId}/available-times")
    public ApiResponse<List<AvailableTimeSlot>> getAvailableTimeSlots(
            @PathVariable Long practiceRoomDetailId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        List<AvailableTimeSlot> availableTimeSlots = practiceRoomDetailService.getPracticeRoomDetailAvailableTimeSlots(practiceRoomDetailId, date);
        return ApiResponse.onSuccess(availableTimeSlots);
    }
}
