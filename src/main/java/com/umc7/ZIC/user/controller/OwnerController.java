package com.umc7.ZIC.user.controller;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.converter.UserConverter;
import com.umc7.ZIC.user.domain.enums.RoleType;
import com.umc7.ZIC.user.dto.UserRequestDto;
import com.umc7.ZIC.user.dto.UserResponseDto;
import com.umc7.ZIC.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner")
public class OwnerController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    //추가정보 기입
    @PatchMapping("/details")
    public ApiResponse<UserResponseDto.userDetailsDto> ownerDetails(@RequestBody UserRequestDto.ownerDetailsDto ownerRequestDto){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(userService.updateOwnerDetails(userId, ownerRequestDto));
    }

    //test용
    @GetMapping("/test/get-token-from-user-id")
    public String testCreateOwnerToken(@RequestParam Long userId){
        String userToken =jwtTokenProvider.createRefreshToken(userId, RoleType.OWNER.name());
        return userToken;
    }

    @Operation(summary = "연습실 주인의 수익 통계를 조회하는 API",
            description = "연습실 주인의 수익 통계를 조회하는 API입니다. JWT 토큰 필요. 날짜는 [년도, 월]을 통해 조회하며 [일]은 아무거나 넣으셔도 됩니다.")
    @Parameters({
            @Parameter(name = "date", description = "조회할 예약 날짜, yyyy-MM-dd 형식으로 입력 ex) 2025-01-01, query String 입니다!"),
    })
    @GetMapping("/revenue")
    public ApiResponse<UserResponseDto.OwnerEarningDTO> ownerJWTRevenue(
            @RequestParam(name = "date") LocalDate date
    ) {
        if (jwtTokenProvider.resolveAccessToken().isEmpty()) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        } else if (!Objects.equals(jwtTokenProvider.getUserTypeFromToken(), RoleType.OWNER.name())) {
            throw new UserHandler(ErrorStatus.PRACTICEROOM_NOT_OWNER_ROLE);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken();
        List<UserResponseDto.OwnerEarning> ownerEarningList = userService.getOwnerEarnings(userId, date);
        List<UserResponseDto.OwnerMonthlyEarning> ownerMonthlyEarningList = userService.getOwnerMonthlyEarnings(userId);

        return ApiResponse.onSuccess(UserConverter.toOwnerEarningDTO(ownerEarningList, ownerMonthlyEarningList));
    }
}
