package com.umc7.ZIC.user.dto;

import java.util.List;

public record UserRequestDto(

) {
    public static record joinDto (
            String email,
            String name,
            String region,
            List<String> instrument
    ){}
    public static record userDetailsDto (
            String region,
            List<String> instrumentList
    ){}
    public static record ownerDetailsDto (
            String region1,
            String region2,
            List<String> instrumentList,
            String businessNumber,
            String businessName
    ){}

//    public static record testUserJoinDto(
//            String kakaoId,
//            String name,
//            String email,
//            String region,
//            List<String> InstrumentList
//
//    ){}
}
