package com.umc7.ZIC.practiceRoom.service;

import com.umc7.ZIC.practiceRoom.dto.AvailableTimeSlot;
import com.umc7.ZIC.practiceRoom.dto.PageRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailResponseDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;


public interface PracticeRoomDetailService {
    PracticeRoomDetailResponseDto.CreateDetailResponseDto createPracticeRoomDetail(PracticeRoomDetailRequestDto.CreateRequestDetailDto createRequest, Long practiceRoomId, Long userId);
    Page<PracticeRoomDetailResponseDto.GetDetailResponseDto> getPracticeRoomDetailList(PageRequestDto request, Long practiceRoomId);
    PracticeRoomDetailResponseDto.GetDetailResponseDto getPracticeRoomDetail(Long practiceRoomDetailId);
    PracticeRoomDetailResponseDto.UpdateDetailResponseDto updatePracticeRoomDetail(PracticeRoomDetailRequestDto.UpdateRequestDetailDto updateRequest, Long practiceRoomDetailId, Long userId);
    void deletePracticeRoomDetail(Long practiceRoomDetailId, Long userId);
    List<AvailableTimeSlot> getPracticeRoomDetailAvailableTimeSlots(Long practiceRoomDetailId, LocalDate date);
}
