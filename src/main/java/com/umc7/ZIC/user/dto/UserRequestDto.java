package com.umc7.ZIC.user.dto;

import java.util.List;

public record UserRequestDto(

) {
    public static record joinDto (
            String email,
            String name,
            String region,
            List<String> instrument
    ){

    }
}
