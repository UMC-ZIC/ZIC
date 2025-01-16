package com.umc7.ZIC.reservation.converter;

import com.umc7.ZIC.reservation.dto.PaymentRequestDTO;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class KakaoPayConverter {

    /**
     * KAKAO PAY 결제 요청을 하기위한 JSON BODY 생성
     * @param cid 가맹점 고유 코드 : 테스트 코드는 TC0ONETIME
     * @param partner_order_id
     * @param partner_user_id
     * @param item PracticeRoom.getName() + " " + PracticeRoomDetail.getName()
     * @param startTime 예약 시작 시간
     * @param endTime 예약 끝나는 시간
     * @param price 연습실 시간당 대여비
     * @return
     */
    public static Map<String, String> toReadyParam(
            String cid, String partner_order_id,
            String partner_user_id, String item,
            LocalTime startTime, LocalTime endTime,
            int price) {
        Duration duration = Duration.between(startTime, endTime);
        Long hours = duration.toHours();
        String quantity = String.valueOf(hours);

        long amount = hours * price;
        long vat = amount / 10;

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("partner_order_id", partner_order_id);
        parameters.put("partner_user_id", partner_user_id);
        parameters.put("item_name", item);
        parameters.put("quantity", quantity);
        parameters.put("total_amount", String.valueOf(amount + vat));
        parameters.put("vat_amount", String.valueOf(vat));
        parameters.put("tax_free_amount", String.valueOf(amount));
        parameters.put("approval_url", "http://localhost:8080/api/reservation/payment/success"); // 성공 시 redirect url
        parameters.put("cancel_url", "http://localhost:8080/api/reservation/payment/cancel"); // 취소 시 redirect url
        parameters.put("fail_url", "http://localhost:8080/api/reservation/payment/fail"); // 실패 시 redirect url

        return parameters;
    }

    /**
     * KAKAO PAY 결제 승인을 하기위한 JSON BODY 생성
     * @param cid 가맹점 고유 코드 : 테스트 코드는 TC0ONETIME
     * @param request 
     * @return
     */
    public static Map<String, String> toApproveParam(String cid, PaymentRequestDTO.KakaoPaymentApproveRequestDTO request) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", request.tid());
        parameters.put("partner_order_id", request.partner_order_id());
        parameters.put("partner_user_id", request.partner_user_id());
        parameters.put("pg_token", request.pg_token());

        return parameters;
    }
}
