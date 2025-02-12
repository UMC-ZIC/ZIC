package com.umc7.ZIC.user.dto;

import lombok.Builder;


import java.util.List;

public record UserResponseDto() {

    @Builder
    public record User(
            String email,
            String name,
            Long regionId
    ){
        @Builder
        public record userDetailsDto(
                Long userId,
                String userName,
                String userRole, //user인지 owner인지
                String token
        ){}
        @Builder
        public record OwnerDetailsDto(
                Long userId,
                String userName,
                String userRole, //user인지 owner인지
                String token,
                Long practiceRoomId
        ){}
    }


    /**
     * 연습실 주인의 연습실 방별 예약 횟수와 수익
     * @param userId
     * @param practiceRoomId
     * @param practiceRoomName
     * @param practiceRoomDetail 연습실 방별 수익
     */
    @Builder
    public record OwnerEarning(
            Long userId,
            Long practiceRoomId,
            String practiceRoomName,
            List<PracticeRoomDetailStat> practiceRoomDetail
    ) {
        @Builder
        public record PracticeRoomDetailStat(
                Long practiceRoomDetailId,
                String practiceRoomDetailName,
                Integer fee,
                Long reservationCount,
                Integer totalRevenue
        ) {}
    }

    /**
     * 연습실 월별 누적 수익
     * @param year
     * @param month
     * @param totalRevenue
     */
    @Builder
    public record OwnerMonthlyEarning(
            Integer year,
            Integer month,
            Integer totalRevenue
    ) {}

    /**
     * 연습실 주인에게 전달할 수익 DTO
     * @param practiceRoomEarning
     * @param monthlyEarning
     */
    @Builder
    public record OwnerEarningDTO(
            List<OwnerEarning> practiceRoomEarning,
            List<OwnerMonthlyEarning> monthlyEarning
    ) {}

    @Builder
    public record UserMyPageDto(
            String userName,
            UserThisMonthPractice userThisMonthPractices,
            FrequentPracticeRooms frequentPracticeRooms
    ){
        @Builder
        public record UserThisMonthPractice(
                List<UserThisMonthPracticeDetail> userThisMonthPracticeList,
                int totalPracticeCount
        ){
            @Builder
            public record UserThisMonthPracticeDetail(
                    String practiceRoomName,
                    String practiceRoomNameDetail,
                    int practiceCount
            ){}
        }

        @Builder
        public record FrequentPracticeRooms(
                List<FrequentPracticeRoomDetail> frequentPracticeRoomDetailList
        ){
            @Builder
            public record FrequentPracticeRoomDetail(
                    String roomName
            ){}
        }
    }

}
