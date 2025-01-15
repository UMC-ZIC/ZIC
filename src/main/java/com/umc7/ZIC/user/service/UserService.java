package com.umc7.ZIC.user.service;

import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.dto.UserRequestDto;

public interface UserService {
    User join(String kakaoCode, UserRequestDto.joinDto joinDto);

}
