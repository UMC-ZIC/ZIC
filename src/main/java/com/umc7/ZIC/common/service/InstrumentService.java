package com.umc7.ZIC.common.service;

import com.umc7.ZIC.common.domain.Instrument;


public interface InstrumentService {
    Instrument findInstrumentByName(String instrumentName);
}
