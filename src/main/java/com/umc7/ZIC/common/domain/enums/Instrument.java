package com.umc7.ZIC.common.domain.enums;

import lombok.Getter;

@Getter
public enum Instrument {
    piano("피아노"),
    violin("바이올린"),
    guitar("기타"),
    bass("베이스"),
    drum("드럼"),
    flute("플롯"),
    harp("하프"),
    trumpet("트럼펫"),
    viola("비올라"),
    samulnori("사물놀이");

    private final String koreanName;


    Instrument(String koreanName) {
        this.koreanName = koreanName;
    }
}
