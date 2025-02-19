package com.umc7.ZIC.common.service;

import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.InstrumentHandler;
import com.umc7.ZIC.common.domain.Instrument;
import com.umc7.ZIC.common.domain.enums.InstrumentType;
import com.umc7.ZIC.common.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InstrumentServiceImpl implements InstrumentService {

    private final InstrumentRepository instrumentRepository;

    @Override
    public Instrument findInstrumentByName(String instrumentName) {
        InstrumentType instrumentType ;
        try {
            instrumentType = InstrumentType.valueOf(instrumentName.toUpperCase()); // 영문 enum 상수
        } catch (IllegalArgumentException e) {
            try {
                instrumentType = InstrumentType.fromKoreanName(instrumentName); // 한글 이름
            } catch (IllegalArgumentException e2) {
                throw new InstrumentHandler(ErrorStatus.INSTRUMENT_NOT_FOUND);
            }
        }

        return instrumentRepository.findByName(instrumentType)
                .orElseThrow(() -> new InstrumentHandler(ErrorStatus.INSTRUMENT_NOT_FOUND));


    }
}