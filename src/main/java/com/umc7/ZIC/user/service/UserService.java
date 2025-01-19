package com.umc7.ZIC.user.service;

import com.umc7.ZIC.user.dto.UserRequestDto;
import com.umc7.ZIC.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto.userDetailsDto updateUserDetails(Long UserId, UserRequestDto.userDetailsDto userDetailsDto);
    UserResponseDto.userDetailsDto updateOwnerDetails(Long UserId, UserRequestDto.ownerDetailsDto ownerDetailsDto);

}
