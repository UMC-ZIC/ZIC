package com.umc7.ZIC.user.converter;


import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.dto.UserResponseDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UserConverter {
    public static UserResponseDto.user.userDetailsDto toRegisterUserDetails(User user, String jwtToken){

        return UserResponseDto.user.userDetailsDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userRole(user.getRole().toString())
                .token(jwtToken)
                .build();
    }

    public static UserResponseDto.user.OwnerDetailsDto toRegisterOwnerDetails(User user, String jwtToken, Long practiceRoomId){

        return UserResponseDto.user.OwnerDetailsDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userRole(user.getRole().toString())
                .token(jwtToken)
                .practiceRoomId(practiceRoomId)
                .build();
    }

    public static UserResponseDto.user.userDetailsDto toResponseUser(User user, String jwtToken){
        return UserResponseDto.user.userDetailsDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userRole(user.getRole().toString())
                .token(jwtToken)
                .build();
    }

    /**
     * 연습실 주인에게 전달할 수익 DTO 객체를 생성
     * @param ownerEarning
     * @param ownerMonthlyEarning
     * @return
     */
    public static UserResponseDto.OwnerEarningDTO toOwnerEarningDTO(List<UserResponseDto.OwnerEarning> ownerEarning, List<UserResponseDto.OwnerMonthlyEarning> ownerMonthlyEarning) {
        return UserResponseDto.OwnerEarningDTO.builder()
                .practiceRoomEarning(ownerEarning)
                .monthlyEarning(ownerMonthlyEarning)
                .build();
    }
}
