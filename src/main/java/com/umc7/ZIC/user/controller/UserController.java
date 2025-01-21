package com.umc7.ZIC.user.controller;

import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.converter.UserConverter;
import com.umc7.ZIC.user.domain.enums.RoleType;
import com.umc7.ZIC.user.dto.UserRequestDto;
import com.umc7.ZIC.user.dto.UserResponseDto;
import com.umc7.ZIC.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


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

    //todo 추가 정보 기입
    @GetMapping("/owner/revenue/{userId}")
    public ApiResponse<UserResponseDto.OwnerEarningDTO> ownerRevenue(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "date") LocalDate date
    ) {
        List<UserResponseDto.OwnerEarning> ownerEarningList = userService.getOwnerEarnings(userId, date);
        List<UserResponseDto.OwnerMonthlyEarning> ownerMonthlyEarningList = userService.getOwnerMonthlyEarnings(userId);

        return ApiResponse.onSuccess(UserConverter.toOwnerEarningDTO(ownerEarningList, ownerMonthlyEarningList));
    }

    @PatchMapping("/owner-details")
    public ApiResponse<UserResponseDto.userDetailsDto> ownerDetails(@RequestBody UserRequestDto.ownerDetailsDto ownerRequestDto){
        Long userId = jwtTokenProvider.getUserIdFromToken();
        return ApiResponse.onSuccess(userService.updateOwnerDetails(userId, ownerRequestDto));
    }

    //test용
    @GetMapping("/test/user/get-token-from-user-id")
    public String testCreateUserToken(@RequestParam Long userId){
        String userToken =jwtTokenProvider.createRefreshToken(userId, RoleType.USER.name());
        return userToken;
    }

    //test용
    @GetMapping("/test/owner/get-token-from-user-id")
    public String testCreateOwnerToken(@RequestParam Long userId){
        String userToken =jwtTokenProvider.createRefreshToken(userId, RoleType.OWNER.name());
        return userToken;
    }

}
