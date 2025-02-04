package com.umc7.ZIC.user.converter;

import com.umc7.ZIC.common.domain.Instrument;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.domain.UserInstrument;

public class UserInstrumentConverter {

    public static UserInstrument toUserInstrument(User user, Instrument instrument) {

        return UserInstrument.builder()
                .user(user)
                .instrument(instrument)
                .build();
    }
}
