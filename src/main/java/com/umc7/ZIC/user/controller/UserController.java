package com.umc7.ZIC.user.controller;

import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.converter.UserConverter;
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

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) throws IOException {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/kakao/join")
    public ApiResponse<?> kakaoJoin(@RequestParam("code") String code, @RequestBody UserRequestDto.joinDto joinDto) {
        userService.join(code, joinDto);

        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/user-id")
    public String userid(){
        String jwt =jwtTokenProvider.getUserIdFromToken().toString()+ "    "+jwtTokenProvider.getUserTypeInToken(jwtTokenProvider.resolveAccessToken()).toString();
        return jwt;
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

}
