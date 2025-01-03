package com.umc7.ZIC.common.domain.enums;

import lombok.Getter;

@Getter
public enum RegionType {
    seoul("서울특별시"),
    busan("부산광역시"),
    daegu("대구광역시"),
    incheon("인천광역시"),
    gwangju("광주광역시"),
    daejeon("대전광역시"),
    ulsan("울산광역시"),
    sejong("세종특별자치시"),
    gyeonggi("경기도"),
    gangwon("강원특별자치도"),
    chungcheongbuk("충청북도"),
    chungcheongnam("충청남도"),
    jeonbuk("전북특별자치도"),
    jeollanam("전라남도"),
    gyeongsangbuk("경상북도"),
    gyeongsangnam("경상남도"),
    jeju("제주특별자치도");

    private final String koreanName;

    RegionType(String koreanName) {
        this.koreanName = koreanName;
    }
}
