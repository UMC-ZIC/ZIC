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
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    //test용
    @GetMapping("/user-id")
    public String userid(){
        String jwt =jwtTokenProvider.getUserIdFromToken().toString()+ "    "+jwtTokenProvider.getUserTypeInToken(jwtTokenProvider.resolveAccessToken()).toString();
        return jwt;
    }

    @PatchMapping("details")
    public ApiResponse<UserResponseDto.userDetailsDto> userDetails(@RequestBody UserRequestDto.userDetailsDto userRequestDto){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(userService.updateUserDetails(userId, userRequestDto));
    }

    //test용
    @GetMapping("/test/get-token-from-user-id")
    public String testCreateUserToken(@RequestParam Long userId){
        String userToken =jwtTokenProvider.createRefreshToken(userId, RoleType.USER.name());
        return userToken;
    }



}
