package com.umc7.ZIC.common.util;

import com.umc7.ZIC.common.domain.enums.RegionType;

import java.util.Arrays;

public class RegionUtil {

    public static RegionType fromKoreanName(String koreanName) {
        return Arrays.stream(RegionType.values())
                .filter(type -> type.getKoreanName().equals(koreanName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid instrument name: " + koreanName));
    }
}
