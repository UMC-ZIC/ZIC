package com.umc7.ZIC.user.repository;


import com.umc7.ZIC.user.dto.UserResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface UserRepositoryCustom {

    UserResponseDto.User findUserByEmail(String email);

    /**
     * 연습실 주인 연습실 방별 수익 및 예약 횟수 조회 Repository
     * @param userId
     * @param targetMonth
     * @return
     */
    List<UserResponseDto.OwnerEarning> findOwnerEarningByUserIdAndMonth(Long userId, LocalDate targetMonth);

    /**
     * 연습실 주인의 월별 누적 수익 조회 Repository
     * @param userId
     * @return
     */
    List<UserResponseDto.OwnerMonthlyEarning> findOwnerMonthlyEarningByUserId(Long userId);
}
