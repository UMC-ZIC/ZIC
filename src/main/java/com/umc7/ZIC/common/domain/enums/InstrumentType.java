package com.umc7.ZIC.common.domain.enums;

import lombok.Getter;

@Getter
public enum InstrumentType {
    PIANO("피아노"),
    VIOLIN("바이올린"),
    GUITAR("기타"),
    BASS("베이스"),
    DRUM("드럼"),
    FLUTE("플룻"),
    HARP("하프"),
    TRUMPET("트럼펫"),
    VIOLA("비올라"),
    SAMULNORI("사물놀이");

    private final String koreanName;


    InstrumentType(String koreanName) {
        this.koreanName = koreanName;
    }

    // 한글 이름을 입력받아 InstrumentType을 반환하는 정적 메서드 추가
    public static InstrumentType fromKoreanName(String koreanName) {
        for (InstrumentType type : InstrumentType.values()) {
            if (type.getKoreanName().equals(koreanName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("InstrumentType : " + koreanName);
    }
}
