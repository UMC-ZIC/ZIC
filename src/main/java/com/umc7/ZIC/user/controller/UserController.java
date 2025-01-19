package com.umc7.ZIC.user.controller;

import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.dto.UserRequestDto;
import com.umc7.ZIC.user.dto.UserResponseDto;
import com.umc7.ZIC.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


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

    @PatchMapping("/user-details")
    public ApiResponse<UserResponseDto.userDetailsDto> userDetails(@RequestBody UserRequestDto.userDetailsDto userRequestDto){
        Long userId = jwtTokenProvider.getUserIdFromToken();

        return ApiResponse.onSuccess(userService.updateUserDetails(userId, userRequestDto));
    }

    @PatchMapping("/owner-details")
    public ApiResponse<UserResponseDto.userDetailsDto> ownerDetails(@RequestBody UserRequestDto.ownerDetailsDto ownerRequestDto){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(userService.updateOwnerDetails(userId, ownerRequestDto));
    }

}
