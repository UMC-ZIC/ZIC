package com.umc7.ZIC.practiceRoom.converter;

import com.umc7.ZIC.common.domain.Instrument;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoom;
import com.umc7.ZIC.practiceRoom.domain.PracticeRoomInstrument;

public class PracticeRoomInstrumentConverter {
    public static PracticeRoomInstrument toPracticeRoomInstrument(PracticeRoom practiceRoom, Instrument instrument){

        return PracticeRoomInstrument.builder()
                .practiceRoom(practiceRoom)
                .instrument(instrument)
                .build();
    }
}
