package com.umc7.ZIC.practiceRoom.service;

import com.umc7.ZIC.practiceRoom.dto.*;

import java.time.LocalDate;
import java.util.List;


public interface PracticeRoomDetailService {
    PracticeRoomDetailResponseDto.CreateDetailResponseDto createPracticeRoomDetail(PracticeRoomDetailRequestDto.CreateRequestDetailDto createRequest, Long practiceRoomId, Long userId);
    PageResponseDto<PracticeRoomDetailResponseDto.GetDetailResponseDto> getPracticeRoomDetailList(PageRequestDto request, Long practiceRoomId);
    PracticeRoomDetailResponseDto.GetDetailResponseDto getPracticeRoomDetail(Long practiceRoomDetailId);
    PracticeRoomDetailResponseDto.UpdateDetailResponseDto updatePracticeRoomDetail(PracticeRoomDetailRequestDto.UpdateRequestDetailDto updateRequest, Long practiceRoomDetailId, Long userId);
    void deletePracticeRoomDetail(Long practiceRoomDetailId, Long userId);
    List<AvailableTimeSlot> getPracticeRoomDetailAvailableTimeSlots(Long practiceRoomDetailId, LocalDate date);
    PracticeRoomDetailResponseDto.UpdateDetailResponseDto updateStatusPracticeRoomDetail(Long practiceRoomDetailId, Long userId);

    // 대여자용 연습실 내부방 리스트 조회
    PracticeRoomDetailResponseDto.GetOwnerDetailResponseDto getOwnerPracticeRoomDetailList(Long userId);
}
