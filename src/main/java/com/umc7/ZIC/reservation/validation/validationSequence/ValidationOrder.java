package com.umc7.ZIC.reservation.validation.validationSequence;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

public class ValidationOrder {

    /**
     * 결제 DTO 검증 순서 지정 : Exist -> Status Check
     */
    @GroupSequence({
            Default.class,
            ValidationStep.ExistValidation.class,
            ValidationStep.StatusValidation.class,
            ValidationStep.dataValidation.class,
    })
    public interface OrderedKakaoPaymentValidation {}

    /**
     * 예약 DTO 검증 순서 지정 : Exist -> Status Check
     */
    @GroupSequence({
            Default.class,
            ValidationStep.ExistValidation.class,
            ValidationStep.StatusValidation.class,
            ValidationStep.dataValidation.class,
            ValidationStep.timeValidationStep1.class,
            ValidationStep.timeValidationStep2.class,
    })
    public interface OrderedReservationValidation {}
}
