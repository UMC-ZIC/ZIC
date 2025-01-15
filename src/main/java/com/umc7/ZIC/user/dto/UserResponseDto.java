package com.umc7.ZIC.user.dto;

public record UserResponseDto() {

    public static record user(
            String email,
            String name,
            Long regionId
    ){ }
}
