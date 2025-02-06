package com.umc7.ZIC.practiceRoom.service;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomDetailHandler;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomHandler;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.dto.*;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomDetailRepository;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomRepository;
import com.umc7.ZIC.reservation.domain.Reservation;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.domain.enums.RoleType;
import com.umc7.ZIC.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    public PracticeRoomDetailResponseDto.CreateDetailResponseDto createPracticeRoomDetail(PracticeRoomDetailRequestDto.CreateRequestDetailDto createRequest, Long practiceRoomId, Long userId) {

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

            return PracticeRoomDetailResponseDto.CreateDetailResponseDto.from(savedPracticeRoomDetail);
        }catch (Exception e){
            log.info(e.getMessage());
            throw new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOM_NOT_OWNER_ROLE);
        }
    }

    //연습실 내부 방 목록 조회
    @Override
    public PageResponseDto<PracticeRoomDetailResponseDto.GetDetailResponseDto> getPracticeRoomDetailList(PageRequestDto request, Long practiceRoomId) {

        //연습실이 있는지 확인
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId)
                .orElseThrow(() -> new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND));

        //PageRequestDto 객체로부터 Pageable 객체를 생성
        Pageable pageable = request.toPageable();
        Page<PracticeRoomDetail> practiceRoomDetailPage = practiceRoomDetailRepository.findAllByPracticeRoomId(practiceRoomId, pageable);

        // 조회된 내부 방 목록(Page<PracticeRoomDetail>)을 DTO 목록(Page<PracticeRoomDetailResponseDto.GetDetailResponseDto>)으로 변환.
        Page<PracticeRoomDetailResponseDto.GetDetailResponseDto> practiceRoomDetailDtoPage = practiceRoomDetailPage.map(PracticeRoomDetailResponseDto.GetDetailResponseDto::from);

        //DTO 목록(Page<DTO>)을 최종 응답 객체(PageResponseDto<DTO>)로 변환하여 반환.
        return PageResponseDto.from(practiceRoomDetailDtoPage);
    }

    //연습실 단일 조회
    @Override
    public PracticeRoomDetailResponseDto.GetDetailResponseDto getPracticeRoomDetail(Long practiceRoomDetailId) {


        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findById(practiceRoomDetailId)
                .orElseThrow(() -> new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND));

        return PracticeRoomDetailResponseDto.GetDetailResponseDto.from(practiceRoomDetail);
    }

    //연습실 정보 수정
    @Override
    public PracticeRoomDetailResponseDto.UpdateDetailResponseDto updatePracticeRoomDetail(PracticeRoomDetailRequestDto.UpdateRequestDetailDto updateRequest, Long practiceRoomDetailId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findById(practiceRoomDetailId)
                .orElseThrow(() -> new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND));

        PracticeRoom practiceRoom = practiceRoomDetail.getPracticeRoom();

        // 소유자 권한 및 연습실 소유 여부 확인
        if (!user.getRole().equals(RoleType.OWNER) || !practiceRoom.getUser().getId().equals(userId)) {
            throw new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_OWNER_ROLE);
        }

        practiceRoomDetail.update(
                updateRequest.name() != null ? updateRequest.name() : practiceRoomDetail.getName(),
                updateRequest.image() != null ? updateRequest.image() : practiceRoomDetail.getImage(),
                updateRequest.fee() != null ? updateRequest.fee() : practiceRoomDetail.getFee(),
                updateRequest.status() != null ? updateRequest.status() : practiceRoomDetail.getStatus()
        );

        PracticeRoomDetail updatedPracticeRoomDetail = practiceRoomDetailRepository.save(practiceRoomDetail);

        return PracticeRoomDetailResponseDto.UpdateDetailResponseDto.from(updatedPracticeRoomDetail);
    }

    // 연습실 내부 방 삭제
    @Override
    public void deletePracticeRoomDetail(Long practiceRoomDetailId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findById(practiceRoomDetailId)
                .orElseThrow(() -> new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND));

        PracticeRoom practiceRoom = practiceRoomDetail.getPracticeRoom();

        // 소유자 권한 및 연습실 소유 여부 확인
        if (!user.getRole().equals(RoleType.OWNER) || !practiceRoom.getUser().getId().equals(userId)) {
            throw new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_OWNER_ROLE);
        }
        practiceRoomDetailRepository.delete(practiceRoomDetail);
    }

    //예약 가능한 시간 조회
    public List<AvailableTimeSlot> getPracticeRoomDetailAvailableTimeSlots(Long practiceRoomDetailId, LocalDate date) {
        PracticeRoomDetail practiceRoomDetail = practiceRoomDetailRepository.findById(practiceRoomDetailId)
                .orElseThrow(() -> new PracticeRoomDetailHandler(ErrorStatus.PRACTICEROOMDETAIL_NOT_FOUND));

        List<Reservation> reservations = practiceRoomDetail.getSuccessReservationsByDate(date);
        List<AvailableTimeSlot> availableTimeSlots = new ArrayList<>();

        // 영업 시작 시간과 종료 시간 (예: 09시 ~ 22시)
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(22, 0);

        // 30분 단위로 시간 슬롯을 확인
        while (startTime.isBefore(endTime)) {
            LocalTime currentStartTime = startTime;
            LocalTime currentEndTime = startTime.plusHours(1);
            // 현재 시간 슬롯이 예약 가능한지 확인
            boolean isAvailable = true;
            for (Reservation reservation : reservations) {
                // 예약 시작 시간과 끝 시간을 가져옴
                LocalTime reservationStartTime = reservation.getStartTime();
                LocalTime reservationEndTime = reservation.getEndTime();

                // 현재 시간 슬롯이 예약 시간과 겹치는지 확인하는 과정
                if (currentStartTime.isBefore(reservationEndTime) && currentEndTime.isAfter(reservationStartTime)) {
                    isAvailable = false;
                    break; // 겹치는 예약을 발견하면 더 이상 확인할 필요가 없습니다.
                }
            }

            // 현재 시간 슬롯이 예약 가능하면
            if (isAvailable) {
                // 가능한 시간 슬롯의 끝 시간을 찾기
                while (currentEndTime.isBefore(endTime)) {
                    boolean canExtend = true;
                    for (Reservation reservation : reservations) {
                        if (currentEndTime.isBefore(reservation.getEndTime()) && currentEndTime.plusHours(1).isAfter(reservation.getStartTime())) {
                            canExtend = false;
                            break;
                        }
                    }
                    if (!canExtend) break;
                    currentEndTime = currentEndTime.plusHours(1);
                }
                // 가능한 시간 슬롯을 목록에 추가합니다.
                availableTimeSlots.add(new AvailableTimeSlot(currentStartTime, currentEndTime));
                // 시작 시간을 현재 끝 시간으로 설정하여 다음 슬롯을 탐색
                startTime = currentEndTime;
            } else {
                // 다음 시간 슬롯으로 이동.
                startTime = startTime.plusHours(1);
            }
        }

        return availableTimeSlots;
    }
}
