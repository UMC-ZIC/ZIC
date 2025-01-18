package com.umc7.ZIC.practiceRoom.domain.enums;

import lombok.Getter;

@Getter
public enum RoomStatus {
    AVAILABLE("이용가능"),
    SUSPENDED("이용중지"),
    ETC("기타");

    private final String koreanName;


    RoomStatus(String koreanName) {
        this.koreanName = koreanName;
    }
}