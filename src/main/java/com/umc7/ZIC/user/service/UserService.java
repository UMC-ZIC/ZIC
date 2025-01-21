package com.umc7.ZIC.user.service;

import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.dto.UserRequestDto;
import com.umc7.ZIC.user.dto.UserResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User join(String kakaoCode, UserRequestDto.joinDto joinDto);

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
}
