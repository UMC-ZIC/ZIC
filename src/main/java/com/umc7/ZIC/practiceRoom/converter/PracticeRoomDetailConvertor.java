package com.umc7.ZIC.practiceRoom.converter;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailResponseDto;

import java.util.List;

public class PracticeRoomDetailConvertor {
    public static List<PracticeRoomDetailResponseDto.GetOwnerDetailResponseDto.PracticeRoomDetailDTO> toPracticeRoomDetails(List<PracticeRoomDetail> practiceRoomDetails) {
        return practiceRoomDetails.stream()
                .map((practiceRoomDetail) ->PracticeRoomDetailResponseDto.GetOwnerDetailResponseDto.PracticeRoomDetailDTO.builder()
                        .practiceRoomDetailId(practiceRoomDetail.getId())
                        .img(practiceRoomDetail.getImage())
                        .fee(practiceRoomDetail.getFee())
                        .name(practiceRoomDetail.getName())
                        .status(practiceRoomDetail.getStatus())
                        .build()
                )
                .toList();
    }
}
