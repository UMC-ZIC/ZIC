package com.umc7.ZIC.practiceRoom.dto;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomDetail;
import com.umc7.ZIC.practiceRoom.domain.enums.RoomStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record PracticeRoomDetailRequestDto() {
    public record CreateRequestDetailDto(
            @NotBlank(message = "연습방 이름은 필수입니다.")
            @Size(max = 20, message = "연습방 이름은 20자 이하여야 합니다.")
            String name,
            String image,
            @NotNull(message = "가격은 필수입니다.")
            @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
            @Size(max = 20, message = "가격은 20자 이하여야 합니다.")
            Integer fee,
            @NotNull(message = "이용 상태는 필수입니다.")
            RoomStatus status
    ) {
        public PracticeRoomDetail toEntity(PracticeRoom practiceRoom) {
            return PracticeRoomDetail.builder()
                    .practiceRoom(practiceRoom)
                    .name(this.name)
                    .image(this.image)
                    .fee(this.fee)
                    .status(this.status)
                    .build();
        }
    }

    public record UpdateRequestDetailDto(
            String name,
            String image,
            @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
            Integer fee,
            RoomStatus status
    ) {
    }
}