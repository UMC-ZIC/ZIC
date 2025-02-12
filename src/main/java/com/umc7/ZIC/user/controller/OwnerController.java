package com.umc7.ZIC.user.controller;

import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.converter.UserConverter;
import com.umc7.ZIC.user.dto.UserRequestDto;
import com.umc7.ZIC.user.dto.UserResponseDto;
import com.umc7.ZIC.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "대여자", description = "대여자 api")
@RequestMapping("/api/owner")
public class OwnerController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    //추가정보 기입
    @Operation(summary = "유저 회원가입 할 때 사용하는 API", description = "유저가 로그인 후 추가 정보 기입후 최종 가입 할 때 사용하는 API")
    @PatchMapping("/details")
    public ApiResponse<UserResponseDto.UserDetailsDto> ownerDetails(@RequestBody UserRequestDto.ownerDetailsDto ownerRequestDto){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(userService.updateOwnerDetails(userId, ownerRequestDto));
    }

    @Operation(summary = "연습실 주인의 수익 통계를 조회하는 API",
            description = "연습실 주인의 수익 통계를 조회하는 API입니다. " +
                    "<br>날짜는 <b>[년도, 월]</b>을 통해 조회하며 <b>[일]은 아무거나 넣으셔도 됩니다.</b>" +
                    "<br><h2>JWT 토큰 필요합니다.</h2> ")
    @Parameters({
            @Parameter(name = "date", description = "조회할 예약 날짜, yyyy-MM-dd 형식으로 입력 ex) 2025-01-01, query String 입니다!"),
    })
    @GetMapping("/revenue")
    public ApiResponse<UserResponseDto.OwnerEarningDTO> ownerJWTRevenue(
            @RequestParam(name = "date") LocalDate date
    ) {
        Long userId = jwtTokenProvider.getUserIdFromToken();
        List<UserResponseDto.OwnerEarning> ownerEarningList = userService.getOwnerEarnings(userId, date);
        List<UserResponseDto.OwnerMonthlyEarning> ownerMonthlyEarningList = userService.getOwnerMonthlyEarnings(userId);

        return ApiResponse.onSuccess(UserConverter.toOwnerEarningDTO(ownerEarningList, ownerMonthlyEarningList));
    }
}
