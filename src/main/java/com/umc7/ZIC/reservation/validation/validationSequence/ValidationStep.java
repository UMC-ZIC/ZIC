package com.umc7.ZIC.reservation.validation.validationSequence;

public class ValidationStep {

    /**
     * 존재하는 데이터인지 검증하는 Validation 그룹
     */
    public interface ExistValidation {}

    /**
     * Status를 검증하는 Validation 그룹
     */
    public interface StatusValidation {}

    /**
     * 데이터를 검증하는 Validation 그룹
     */
    public interface dataValidation {}

    /**
     * 예약 시간 검증하는 첫번째 단계 Validation 그룹
     */
    public interface timeValidationStep1{}

    /**
     * 예약 시간 검증하는 두번째 단계 Validation 그룹
     */
    public interface timeValidationStep2{}
}
