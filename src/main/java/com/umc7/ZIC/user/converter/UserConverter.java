package com.umc7.ZIC.user.converter;


import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.dto.UserResponseDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UserConverter {
    public static UserResponseDto.UserDetailsDto toRegisterUserDetails(User user, String jwtToken){

        return UserResponseDto.UserDetailsDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userRole(user.getRole().toString())
                .token(jwtToken).build();
    }

    public static UserResponseDto.UserDetailsDto toResponseUser(User user, String jwtToken){
        return UserResponseDto.UserDetailsDto.builder()
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

    public static UserResponseDto.UserMyPageDto.UserThisMonthPractice toUserMyPageDTO(List<UserResponseDto.UserMyPageDto.UserThisMonthPractice.UserThisMonthPracticeDetail> userThisMonthPracticeDetailList) {
        int totalPracticeCount = userThisMonthPracticeDetailList.stream()
                .mapToInt(UserResponseDto.UserMyPageDto.UserThisMonthPractice.UserThisMonthPracticeDetail::practiceCount)
                .sum();

        return UserResponseDto.UserMyPageDto.UserThisMonthPractice.builder()
                .userThisMonthPracticeList(userThisMonthPracticeDetailList)
                .totalPracticeCount(totalPracticeCount)
                .build();
    }

    public static UserResponseDto.UserMyPageDto.FrequentPracticeRooms toFrequentPracticeRooms(List<UserResponseDto.UserMyPageDto.FrequentPracticeRooms.FrequentPracticeRoomDetail> top3Room){
        return UserResponseDto.UserMyPageDto.FrequentPracticeRooms.builder()
                .frequentPracticeRoomDetailList(top3Room)
                .build();
    }

    public static UserResponseDto.UserMyPageDto UserMyPageDto(List<UserResponseDto.UserMyPageDto.FrequentPracticeRooms.FrequentPracticeRoomDetail> top3Room, List<UserResponseDto.UserMyPageDto.UserThisMonthPractice.UserThisMonthPracticeDetail> userThisMonthPracticeDetailList, User user){
        return UserResponseDto.UserMyPageDto.builder()
                .userName(user.getName())
                .userThisMonthPractices(toUserMyPageDTO(userThisMonthPracticeDetailList))
                .frequentPracticeRooms(toFrequentPracticeRooms(top3Room))
                .build();
    }

}
