package com.umc7.ZIC.practiceRoom.controller;


import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.common.domain.Instrument;
import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.common.service.InstrumentService;
import com.umc7.ZIC.common.service.RegionService;
import com.umc7.ZIC.practiceRoom.dto.PageRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PageResponseDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomResponseDto;
import com.umc7.ZIC.practiceRoom.service.PracticeRoomService;
import com.umc7.ZIC.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/practice-rooms")
@RequiredArgsConstructor
@Tag(name = "연습실", description = "연습실 CRUD")
public class PracticeRoomController {

    private final PracticeRoomService practiceRoomService;
    private final RegionService regionService;
    private final InstrumentService instrumentService;
    private final JwtTokenProvider jwtTokenProvider;

    // Practice Room 관련 API
    //연습실 등록
//    @Operation(summary = "연습실을 등록할때 사용하는 API", description = "유저가 Owner 역할일때 본인의 연습실을 등록할 때 사용하는 API")
//    @PostMapping
//    public ApiResponse<PracticeRoomResponseDto.CreateResponseDto> createPracticeRoom(
//            @RequestBody @Valid PracticeRoomRequestDto.CreateRequestDto createRequest) {
//        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
//            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
//        }
//
//        Long userId = jwtTokenProvider.getUserIdFromToken();
//        PracticeRoomResponseDto.CreateResponseDto response = practiceRoomService.createPracticeRoom(createRequest, userId);
//        return ApiResponse.onSuccess(response);
//    }

    //연습실 수정
    @PatchMapping("/{id}")
    @Parameters({
            @Parameter(name = "id", description = "수정할 연습실의 id ")
    })
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
    @Parameters({
            @Parameter(name = "id", description = "조회할 연습실의 id ")
    })
    @Operation(summary = "연습실을 조회할때 사용하는 API", description = "PracticeRoomId로 연습실을 조회할 때 사용하는 API")
    public ApiResponse<PracticeRoomResponseDto.GetResponseDto> getPracticeRoom(@PathVariable Long id) {
        PracticeRoomResponseDto.GetResponseDto response = practiceRoomService.getPracticeRoom(id);
        return ApiResponse.onSuccess(response);
    }

    //연습실 목록 조회
    @GetMapping
    @Parameters({
            @Parameter(name = "date", description = "조회할 날짜, yyyy-MM-dd 형식으로 입력 ex) 2025-01-01, 날짜 null 일시 이용가능한방 0으로 표시"),
            @Parameter(name = "regionName", description = "NULL 일경우 전체 지역 조회입니다, 필터를 적용할 지역 이름 '서울', '부산', 'BUSAN', 'SEOUL' 등등 영문, 한글 둘 다 가능하게 되어있습니다.(정확하게 입력해야합니다.)"),
            @Parameter(name = "instrumentName", description = "NULL 일경우 전체 악기 조회입니다, 필터를 적용할 악기 이름 '드럼', 'DRUM' 등등 영문, 한글 둘 다 가능하게 되어있습니다.(정확하게 입력해야합니다.)"),
            @Parameter(name = "priceSort", description = "가격 정렬 (asc 또는 desc, 대소문자 구분 없음), 아무값이나 넣으면 id ASC 정렬")
    })
    @Operation(summary = "연습실을 목록(페이징) 형식으로조회할때 사용하는 API", description = "연습실을 목록(페이징) 형식으로 조회할 때 사용하는 API (악기 필터링 미구현)")
    public ApiResponse<PageResponseDto<PracticeRoomResponseDto.GetListResponseDto>> getPracticeRoomList(@ModelAttribute PageRequestDto request,
                                                                                                        @RequestParam(name = "date", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                                                                        @RequestParam(name = "regionName", required = false) String regionName,
                                                                                                        @RequestParam(name = "instrumentName", required = false) String instrumentName) {
        Region region = null;
        Instrument instrument = null;
        if (regionName != null) {
            region = regionService.findRegionByName(regionName); // RegionService 사용
        }
        if (instrumentName != null) {
            instrument = instrumentService.findInstrumentByName(instrumentName); // RegionService 사용
        }
        PageResponseDto<PracticeRoomResponseDto.GetListResponseDto> response = practiceRoomService.getPracticeRoomList(request, date, region, instrument);
        return ApiResponse.onSuccess(response);
    }
}