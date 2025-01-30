package com.umc7.ZIC.practiceRoom.service;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomHandler;
import com.umc7.ZIC.apiPayload.exception.handler.RegionHandler;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.common.repository.RegionRepository;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.dto.PageRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PageResponseDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomRequestDto;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomResponseDto;
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

import java.awt.color.ProfileDataException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PracticeRoomServiceImpl implements PracticeRoomService {
    private final PracticeRoomRepository practiceRoomRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    //연습실 등록
    @Override
    public PracticeRoomResponseDto.CreateResponseDto createPracticeRoom(PracticeRoomRequestDto.CreateRequestDto createRequest, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Region region = user.getRegion();

        if (!user.getRole().equals(RoleType.OWNER)){
            throw new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_OWNER_ROLE);
        }
        try {
            PracticeRoom practiceRoom = createRequest.toEntity(user, region);

            PracticeRoom savedPracticeRoom = practiceRoomRepository.save(practiceRoom);

            return PracticeRoomResponseDto.CreateResponseDto.from(savedPracticeRoom);
        }catch (Exception e) {
            log.error(e.getMessage());
            throw new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_OWNER_ROLE);
        }
    }

    //연습실 수정
    @Override
    public PracticeRoomResponseDto.UpdateResponseDto updatePracticeRoom(PracticeRoomRequestDto.UpdateRequestDto updateRequest, Long practiceRoomId, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId)
                .orElseThrow(() -> new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND));

        if (!user.getRole().equals(RoleType.OWNER)){
            throw new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_OWNER_ROLE);
        }
        if(!user.getId().equals(practiceRoom.getUser().getId())) {
            throw new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_AUTHORIZATION_FAILED);
        }

        //엔티티 수정
        practiceRoom.update(
                updateRequest.name() != null ? updateRequest.name() : practiceRoom.getName(),
                updateRequest.address() != null ? updateRequest.address() : practiceRoom.getAddress(),
                updateRequest.latitude() != null ? updateRequest.latitude() : practiceRoom.getLatitude(),
                updateRequest.longitude() != null ? updateRequest.longitude() : practiceRoom.getLongitude()
        );

        PracticeRoom updatedPracticeRoom = practiceRoomRepository.save(practiceRoom);

        return PracticeRoomResponseDto.UpdateResponseDto.from(updatedPracticeRoom);
    }

    //연습실 삭제
    @Override
    public void deletePracticeRoom(Long practiceRoomId,Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId)
                .orElseThrow(() -> new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND));

        if(!user.getId().equals(practiceRoom.getUser().getId())) {
            throw new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_AUTHORIZATION_FAILED);
        }
        practiceRoomRepository.delete(practiceRoom);

    }

    //연습실 단일조회
    @Override
    public PracticeRoomResponseDto.GetResponseDto getPracticeRoom(Long practiceRoomId) {
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId)
                .orElseThrow(() -> new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND));

        return PracticeRoomResponseDto.GetResponseDto.from(practiceRoom);
    }

    //연습실 목록 조회
    @Override
    public Page<PracticeRoomResponseDto.GetResponseDto> getPracticeRoomList(PageRequestDto request) {
        try {
            Pageable pageable = request.toPageable();
            Page<PracticeRoom> practiceRoomPage = practiceRoomRepository.findAllPracticeRoom(pageable);
            return practiceRoomPage.map(PracticeRoomResponseDto.GetResponseDto::from); // 메서드 레퍼런스 사용
        } catch (Exception e) {
            log.error("getPracticeRoomList error: {}", e.getMessage());
            throw new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND);
        }
    }
}