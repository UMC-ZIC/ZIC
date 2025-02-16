package com.umc7.ZIC.practiceRoom.converter;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailResponseDto;

public class PracticeRoomConvertor {
    public static PracticeRoomDetailResponseDto.GetOwnerDetailResponseDto.PracticeRoomDTO toPracticeRoom(PracticeRoom practiceRoom) {
        return PracticeRoomDetailResponseDto.GetOwnerDetailResponseDto.PracticeRoomDTO.builder()
                        .practiceRoomId(practiceRoom.getId())
                        .img(practiceRoom.getImage())
                        .region(practiceRoom.getRegion().getName().getKoreanName())
                        .address(practiceRoom.getAddress())
                        .latitude(practiceRoom.getLatitude())
                        .longitude(practiceRoom.getLongitude())
                        .build();
    }
}
