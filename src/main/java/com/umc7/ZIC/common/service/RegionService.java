package com.umc7.ZIC.common.service;

import com.umc7.ZIC.common.domain.Region;

public interface RegionService {
    Region findRegionByName(String regionName);
}
