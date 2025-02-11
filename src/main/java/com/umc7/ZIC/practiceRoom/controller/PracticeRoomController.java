package com.umc7.ZIC.practiceRoom.controller;


import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.practiceRoom.dto.PageRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PageResponseDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomResponseDto;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomService;
import com.umc7.ZIC.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/practice-rooms")
@RequiredArgsConstructor
@Tag(name = "연습실", description = "연습실 CRUD")
public class PracticeRoomController {

    private final PracticeRoomService practiceRoomService;
    private final JwtTokenProvider jwtTokenProvider;

    // Practice Room 관련 API
    //연습실 등록
    @Operation(summary = "연습실을 등록할때 사용하는 API", description = "유저가 Owner 역할일때 본인의 연습실을 등록할 때 사용하는 API")
    @PostMapping
    public ApiResponse<PracticeRoomResponseDto.CreateResponseDto> createPracticeRoom(
            @RequestBody @Valid PracticeRoomRequestDto.CreateRequestDto createRequest) {
        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        PracticeRoomResponseDto.CreateResponseDto response = practiceRoomService.createPracticeRoom(createRequest, userId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 수정
    @PatchMapping("/{id}")
    @Operation(summary = "연습실을 수정할때 사용하는 API", description = "유저가 Owner 역할일때 본인의 연습실을 수정할 때 사용하는 API")
    public ApiResponse<PracticeRoomResponseDto.UpdateResponseDto> updatePracticeRoom(
            @RequestBody @Valid PracticeRoomRequestDto.UpdateRequestDto updateRequest,
            @PathVariable Long id) {

        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        PracticeRoomResponseDto.UpdateResponseDto response = practiceRoomService.updatePracticeRoom(updateRequest, id, userId);
        return ApiResponse.onSuccess(response);
    }

    //연습실 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "연습실을 삭제할때 사용하는 API", description = "유저가 본인이 등록한 연습실을 삭제할 때 사용하는 API")
    public ApiResponse<Void> deletePracticeRoom(@PathVariable Long id) {

        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        practiceRoomService.deletePracticeRoom(id, userId);
        return ApiResponse.onSuccess(null);
    }

    //연습실 단일 조회
    @GetMapping("/{id}")
    @Operation(summary = "연습실을 조회할때 사용하는 API", description = "PracticeRoomId로 연습실을 조회할 때 사용하는 API")
    public ApiResponse<PracticeRoomResponseDto.GetResponseDto> getPracticeRoom(@PathVariable Long id) {
        PracticeRoomResponseDto.GetResponseDto response = practiceRoomService.getPracticeRoom(id);
        return ApiResponse.onSuccess(response);
    }

    //연습실 리스트 조회
    @GetMapping
    @Operation(summary = "연습실을 목록(페이징) 형식으로조회할때 사용하는 API", description = "PracticeRoomId로 연습실을 목록(페이징) 형식으로 조회할 때 사용하는 API")
    public ApiResponse<PageResponseDto<PracticeRoomResponseDto.GetResponseDto>> getPracticeRoomList(@ModelAttribute PageRequestDto request) {
        PageResponseDto<PracticeRoomResponseDto.GetResponseDto> response = practiceRoomService.getPracticeRoomList(request);
        return ApiResponse.onSuccess(response);
    }
}