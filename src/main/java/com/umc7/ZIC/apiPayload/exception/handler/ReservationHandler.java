package com.umc7.ZIC.apiPayload.exception.handler;

import com.umc7.ZIC.apiPayload.code.BaseErrorCode;

public class ReservationHandler extends GeneralException {
    public ReservationHandler(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
