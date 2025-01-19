package com.umc7.ZIC.user.dto;

import lombok.Builder;

public record UserResponseDto() {

    @Builder
    public static record user(
            String email,
            String name,
            Long regionId
    ){}
    @Builder
    public static record userDetailsDto(
            Long userId,
            String userName,
            String userRole, //user인지 owner인지
            String token
    ){}
}
