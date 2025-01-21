package com.umc7.ZIC.user.controller;

import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.domain.enums.RoleType;
import com.umc7.ZIC.user.dto.UserRequestDto;
import com.umc7.ZIC.user.dto.UserResponseDto;
import com.umc7.ZIC.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
