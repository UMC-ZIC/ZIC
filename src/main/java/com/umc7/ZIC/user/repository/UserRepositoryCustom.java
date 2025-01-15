package com.umc7.ZIC.user.repository;


import com.umc7.ZIC.user.dto.UserResponseDto;

public interface UserRepositoryCustom {

    UserResponseDto.user findUserByEmail(String email);
}
