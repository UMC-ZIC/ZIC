package com.umc7.ZIC.user.controller;

import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.dto.UserRequestDto;
import com.umc7.ZIC.user.dto.UserResponseDto;
import com.umc7.ZIC.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "이용자", description = "이용자 api")
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "유저 회원가입 할 때 사용하는 API", description = "유저가 로그인 후 추가 정보 기입후 최종 가입 할 때 사용하는 API")
    @PatchMapping("/details")
    public ApiResponse<UserResponseDto.User.UserDetailsDto> userDetails(@RequestBody UserRequestDto.userDetailsDto userRequestDto){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(userService.updateUserDetails(userId, userRequestDto));
    }

    @Operation(summary = "마이페이지 API", description = "마이페이지 이번달 연습 횟수, 자주 가는 연습실 API")
    @GetMapping("/mypage")
    public ApiResponse<UserResponseDto.UserMyPageDto> mypage(){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(userService.getUserMypage(userId));
    }
}
