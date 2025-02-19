package com.umc7.ZIC.common.service;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.PracticeRoomHandler;
import com.umc7.ZIC.apiPayload.exception.handler.RegionHandler;
import com.umc7.ZIC.common.domain.Region;
import com.umc7.ZIC.common.domain.enums.RegionType;
import com.umc7.ZIC.common.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    public Region findRegionByName(String regionName) {
        RegionType regionType;
        try {
            // 먼저 영어 이름(enum 상수 이름)으로 시도
            regionType = RegionType.valueOf(regionName);
        } catch (IllegalArgumentException e) {
            try {
                // 영어 이름으로 찾지 못하면 한글 이름으로 시도
                regionType = RegionType.fromKoreanName(regionName);
            } catch (IllegalArgumentException ex) {
                // 한글 이름으로도 찾지 못하면 예외 발생
                throw new RegionHandler(ErrorStatus.REGION_NOT_FOUND);
            }
        }

        return regionRepository.findByName(regionType)
                .orElseThrow(() -> new RegionHandler(ErrorStatus.REGION_NOT_FOUND));
    }
}