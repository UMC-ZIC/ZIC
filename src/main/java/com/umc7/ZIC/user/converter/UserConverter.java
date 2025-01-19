package com.umc7.ZIC.user.converter;


import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.dto.UserResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserConverter {
    public static UserResponseDto.userDetailsDto toRegisterUserDetails(User user, String jwtToken){

        return UserResponseDto.userDetailsDto.builder()
                .userId(user.getId())
                .userName(user.getName())
                .userRole(user.getRole().toString())
                .token(jwtToken).build();
    }

}
