package com.umc7.ZIC.common.domain.enums;

import lombok.Getter;

@Getter
public enum RegionType {
    SEOUL("서울특별시"),
    BUSAN("부산광역시"),
    DAEGU("대구광역시"),
    INCHEON("인천광역시"),
    GWANGJU("광주광역시"),
    DAEJEON("대전광역시"),
    ULSAN("울산광역시"),
    SEJONG("세종특별자치시"),
    GYEONGGI("경기도"),
    GANGWON("강원특별자치도"),
    CHUNGCHEONGBUK("충청북도"),
    CHUNGCHEONGNAM("충청남도"),
    JEONBUK("전북특별자치도"),
    JEOLLANAM("전라남도"),
    GYEONGSANGBUK("경상북도"),
    GYEONGSANGNAM("경상남도"),
    JEJU("제주특별자치도");

    private final String koreanName;

    RegionType(String koreanName) {
        this.koreanName = koreanName;
    }
}
