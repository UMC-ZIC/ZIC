package com.umc7.ZIC.practiceRoom.repository;

import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PracticeRoomRepositoryCustom {
    Page<PracticeRoom> findAvailablePracticeRoomsByRegionAndDateAndInstrument(Long regionId, LocalDate date, Long instrumentId, String priceSort, Pageable pageable);
}