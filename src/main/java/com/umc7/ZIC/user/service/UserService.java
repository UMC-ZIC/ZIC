package com.umc7.ZIC.user.service;

import com.umc7.ZIC.user.dto.KakaoUserInfoResponseDto;
import com.umc7.ZIC.user.dto.UserRequestDto;
import com.umc7.ZIC.user.dto.UserResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    UserResponseDto.User.UserDetailsDto updateUserDetails(Long UserId, UserRequestDto.userDetailsDto userDetailsDto);
    UserResponseDto.User.OwnerDetailsDto updateOwnerDetails(Long UserId, UserRequestDto.ownerDetailsDto ownerDetailsDto);

    UserResponseDto.User.UserDetailsDto getUser(Long UserId, String jwtToken);
    UserResponseDto.User.UserDetailsDto kaKaoGetUser(KakaoUserInfoResponseDto userInfo);
    /**
     * 연습실 주인 연습실 방별 수익 및 예약 횟수 조회 Service
     * @param userId
     * @param targetMonth
     * @return
     */
    List<UserResponseDto.OwnerEarning> getOwnerEarnings(Long userId, LocalDate targetMonth);

    /**
     * 연습실 주인의 월별 누적 수익 조회 Service
     * @param userId
     * @return
     */
    List<UserResponseDto.OwnerMonthlyEarning> getOwnerMonthlyEarnings(Long userId);

    UserResponseDto.UserMyPageDto getUserMypage(Long userId);
}
