package com.umc7.ZIC.practiceRoom.service;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomDetailHandler;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomHandler;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.dto.PageRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomDetailResponseDto;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomDetailRepository;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomRepository;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.domain.enums.RoleType;
import com.umc7.ZIC.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PracticeRoomDetailServiceImpl implements PracticeRoomDetailService {

    private final PracticeRoomRepository practiceRoomRepository;
    private final PracticeRoomDetailRepository practiceRoomDetailRepository;
    private final UserRepository userRepository;

    //연습실 내부 방 등록
    @Override
    public PracticeRoomDetailResponseDto.CreateResponseDto createPracticeRoomDetail(PracticeRoomDetailRequestDto.CreateRequestDetailDto createRequest, Long practiceRoomId, Long userId) {

        userId = 1L; //임시 하드코딩
        practiceRoomId = 1L;//임시 하드코딩

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId)
                .orElseThrow(() -> new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND));

        if (!user.getRole().equals(RoleType.OWNER) || !practiceRoom.getUser().getId().equals(userId)){
            throw new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_OWNER_ROLE);
        }
        try {
            PracticeRoomDetail practiceRoomDetail = createRequest.toEntity(practiceRoom);
            PracticeRoomDetail savedPracticeRoomDetail = practiceRoomDetailRepository.save(practiceRoomDetail);

            return PracticeRoomDetailResponseDto.CreateResponseDto.from(savedPracticeRoomDetail);
        }catch (Exception e){
            log.info(e.getMessage());
            throw new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOM_NOT_OWNER_ROLE);
        }
    }

    //연습실 내부 방 목록 조회
    @Override
    public Page<PracticeRoomDetailResponseDto.GetResponseDto> getPracticeRoomDetailList(PageRequestDto request, Long practiceRoomId) {

        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId)
                .orElseThrow(() -> new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND));

        Pageable pageable = request.toPageable();
        Page<PracticeRoomDetail> practiceRoomDetailPage = practiceRoomDetailRepository.findAllByPracticeRoomId(practiceRoomId, pageable);

        return practiceRoomDetailPage.map(PracticeRoomDetailResponseDto.GetResponseDto::from);
    }

    //연습실 단일 조회
    // TODO : Long room_id,Long practiceRoomId 연습실과 내부 방 2종류의 id를 받는 이유가 뭔가요?
    @Override
    public PracticeRoomDetailResponseDto.GetResponseDto getPracticeRoomDetail(Long room_id,Long practiceRoomId) {
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId)
                .orElseThrow(() -> new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND));

        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findByIdAndPracticeRoom(room_id, practiceRoom)
                .orElseThrow(() -> new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND));

        return PracticeRoomDetailResponseDto.GetResponseDto.from(practiceRoomDetail);
    }

    //연습실 정보 수정
    @Override
    public PracticeRoomDetailResponseDto.UpdateResponseDto updatePracticeRoomDetail(PracticeRoomDetailRequestDto.UpdateRequestDetailDto updateRequest, Long practiceRoomId, Long room_id) {
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId)
                .orElseThrow(() -> new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND));

        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findByIdAndPracticeRoom(room_id, practiceRoom)
                .orElseThrow(() -> new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND));

        practiceRoomDetail.update(
                updateRequest.name() != null ? updateRequest.name() : practiceRoomDetail.getName(),
                updateRequest.image() != null ? updateRequest.image() : practiceRoomDetail.getImage(),
                updateRequest.fee() != null ? updateRequest.fee() : practiceRoomDetail.getFee(),
                updateRequest.status() != null ? updateRequest.status() : practiceRoomDetail.getStatus()
        );

        PracticeRoomDetail updatedPracticeRoomDetail = practiceRoomDetailRepository.save(practiceRoomDetail);

        return PracticeRoomDetailResponseDto.UpdateResponseDto.from(updatedPracticeRoomDetail);
    }

    @Override
    public void deletePracticeRoomDetail(Long practiceRoomId, Long room_id) {
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId)
                .orElseThrow(() -> new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND));

        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findByIdAndPracticeRoom(room_id, practiceRoom)
                .orElseThrow(() -> new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND));

        practiceRoomDetailRepository.delete(practiceRoomDetail);
    }
}