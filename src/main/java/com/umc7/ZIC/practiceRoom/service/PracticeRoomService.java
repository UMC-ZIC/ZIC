package com.umc7.ZIC.practiceRoom.service;

import com.umc7.ZIC.practiceRoom.dto.PageRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PageResponseDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomResponseDto;
import org.springframework.data.domain.Page;

public interface PracticeRoomService {

    // 연습실 등록
    PracticeRoomResponseDto.CreateResponseDto createPracticeRoom(PracticeRoomRequestDto.CreateRequestDto request, Long userId);
    // 연습실 수정
    PracticeRoomResponseDto.UpdateResponseDto updatePracticeRoom(PracticeRoomRequestDto.UpdateRequestDto request, Long practiceRoomId, Long userId);
    // 연습실 삭제
    void deletePracticeRoom(Long practiceRoomId, Long userId);
    // 연습실 단일 조회
    PracticeRoomResponseDto.GetResponseDto getPracticeRoom(Long practiceRoomId);
    // 연습실 리스트 조회
    PageResponseDto<PracticeRoomResponseDto.GetResponseDto> getPracticeRoomList(PageRequestDto request);
}