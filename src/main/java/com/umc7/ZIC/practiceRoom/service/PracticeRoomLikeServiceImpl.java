package com.umc7.ZIC.practiceRoom.service;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomHandler;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomLike;
import com.umc7.ZIC.practiceRoom.dto.PracticeRoomLikeResponseDto;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomLikeRepository;
import com.umc7.ZIC.practiceRoom.repository.PracticeRoomRepository;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PracticeRoomLikeServiceImpl implements PracticeRoomLikeService {

    private final PracticeRoomLikeRepository practiceRoomLikeRepository;
    private final PracticeRoomRepository practiceRoomRepository;
    private final UserRepository userRepository;

    @Override
    public PracticeRoomLikeResponseDto.LikeResponseDto createPracticeRoomLike(Long practiceRoomId, Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 연습실 조회
        PracticeRoom practiceRoom = practiceRoomRepository.findById(practiceRoomId)
                .orElseThrow(() -> new PracticeRoomHandler(ErrorStatus.PRACTICEROOM_NOT_FOUND));

        // 이미 좋아요를 눌렀는지 확인
        Optional<PracticeRoomLike> existingLike = practiceRoomLikeRepository.findByUserAndPracticeRoom(user, practiceRoom);

        if (existingLike.isPresent()) {
            // 좋아요 취소
            practiceRoomLikeRepository.delete(existingLike.get());
            return PracticeRoomLikeResponseDto.LikeResponseDto.from(existingLike.get()); // 취소된 경우에도 기존 LikeResponseDto 반환
        } else {
            // 좋아요 생성
            PracticeRoomLike practiceRoomLike = PracticeRoomLike.builder()
                    .practiceRoom(practiceRoom)
                    .user(user)
                    .build();

            // 좋아요 저장
            PracticeRoomLike savedPracticeRoomLike = practiceRoomLikeRepository.save(practiceRoomLike);
            return PracticeRoomLikeResponseDto.LikeResponseDto.from(savedPracticeRoomLike);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public PracticeRoomLikeResponseDto.LikeCountResponseDto getLikeCount(Long practiceRoomId) {

        // 좋아요 개수 조회
        List<Long> likeList = practiceRoomLikeRepository.findByPracticeRoomId(practiceRoomId);

        return PracticeRoomLikeResponseDto.LikeCountResponseDto.from(likeList);
    }
}