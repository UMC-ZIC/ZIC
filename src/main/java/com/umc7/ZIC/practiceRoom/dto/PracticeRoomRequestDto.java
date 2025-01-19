package com.umc7.ZIC.practiceRoom.dto;

import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.common.domain.enums.RegionType;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.user.domain.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PracticeRoomRequestDto(

) {
    public record CreateRequestDto(
            @NotBlank(message ="연습실 이름은 필수입니다." )
            String name,
            @NotBlank(message = "주소는 필수입니다.")
            String address,
            @NotNull(message = "위도는 필수입니다.")
            @Min(value = -90, message = "위도는 -90 이상이어야 합니다.")
            @Max(value = 90, message = "위도는 90 이하여야 합니다.")
            Double latitude,
            @NotNull(message = "경도는 필수입니다.")
            @Min(value = -180, message = "경도는 -180 이상이어야 합니다.")
            @Max(value = 180, message = "경도는 180 이하여야 합니다.")
            Double longitude

    ) {
        public PracticeRoom toEntity(User user, Region region) {
            return PracticeRoom.builder()
                    .user(user)
                    .region(region)
                    .name(this.name)
                    .address(this.address)
                    .latitude(this.latitude)
                    .longitude(this.longitude)
                    .build();
        }
    }
        public record UpdateRequestDto(
                String name,
                String address,
                @Min(value = -90, message = "위도는 -90 이상이어야 합니다.")
                @Max(value = 90, message = "위도는 90 이하여야 합니다.")
                Double latitude,
                @Min(value = -180, message = "경도는 -180 이상이어야 합니다.")
                @Max(value = 180, message = "경도는 180 이하여야 합니다.")
                Double longitude
        ){
    }
}
