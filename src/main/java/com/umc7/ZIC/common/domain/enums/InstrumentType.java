package com.umc7.ZIC.common.domain.enums;

import lombok.Getter;

@Getter
public enum InstrumentType {
    PIANO("피아노"),
    VIOLIN("바이올린"),
    GUITAR("기타"),
    BASS("베이스"),
    DRUM("드럼"),
    FLUTE("플롯"),
    HARP("하프"),
    TRUMPET("트럼펫"),
    VIOLA("비올라"),
    SAMULNORI("사물놀이");

    private final String koreanName;


    InstrumentType(String koreanName) {
        this.koreanName = koreanName;
    }
}
