package com.umc7.ZIC.common.util;

import com.umc7.ZIC.common.domain.enums.InstrumentType;

import java.util.Arrays;

public class InstrumentUtil {

    public static InstrumentType fromKoreanName(String koreanName) {
        return Arrays.stream(InstrumentType.values())
                .filter(type -> type.getKoreanName().equals(koreanName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid instrument name: " + koreanName));
    }
}
