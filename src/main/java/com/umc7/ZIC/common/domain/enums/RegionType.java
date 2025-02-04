package com.umc7.ZIC.common.domain.enums;

import lombok.Getter;

@Getter
public enum RegionType {
    SEOUL("서울"),
    BUSAN("부산"),
    DAEGU("대구"),
    INCHEON("인천"),
    GWANGJU("광주"),
    DAEJEON("대전"),
    ULSAN("울산"),
    SEJONG("세종"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGCHEONGBUK("충북"),
    CHUNGCHEONGNAM("충남"),
    JEONBUK("전북"),
    JEOLLANAM("전남"),
    GYEONGSANGBUK("경북"),
    GYEONGSANGNAM("경남"),
    JEJU("제주");

    private final String koreanName;

    RegionType(String koreanName) {
        this.koreanName = koreanName;
    }
}
