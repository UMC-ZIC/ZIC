package com.umc7.ZIC.practiceRoom.service;

import com.umc7.ZIC.practiceRoom.dto.PageRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailResponseDto;
import org.springframework.data.domain.Page;


public interface PracticeRoomDetailService {
    PracticeRoomDetailResponseDto.CreateResponseDto createPracticeRoomDetail(PracticeRoomDetailRequestDto.CreateRequestDetailDto createRequest, Long practiceRoomId, Long userId);
    Page<PracticeRoomDetailResponseDto.GetResponseDto> getPracticeRoomDetailList(PageRequestDto request, Long practiceRoomId);
    PracticeRoomDetailResponseDto.GetResponseDto getPracticeRoomDetail(Long practiceRoomId, Long roomId);
    PracticeRoomDetailResponseDto.UpdateResponseDto updatePracticeRoomDetail(PracticeRoomDetailRequestDto.UpdateRequestDetailDto updateRequest, Long practiceRoomId, Long roomId, Long userId);
    void deletePracticeRoomDetail(Long practiceRoomId, Long roomId, Long userId);
}
