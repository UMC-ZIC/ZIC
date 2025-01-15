package com.umc7.ZIC.apiPayload.exception.handler;

import com.umc7.ZIC.apiPayload.code.BaseErrorCode;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}
