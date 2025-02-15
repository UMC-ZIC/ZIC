package com.umc7.ZIC.reservation.converter;

import com.umc7.ZIC.reservation.dto.PaymentRequestDTO;
import org.springframework.http.HttpHeaders;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class KakaoPayConverter {

    /**
     * JSON 헤더 추가
     * @param secret_Key_Dev KaKao Pay Secret_Key_DEV
     * @return
     */
    public static HttpHeaders getHeaders(String secret_Key_Dev) {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "SECRET_KEY " + secret_Key_Dev;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-Type", "application/json");

        return httpHeaders;
    }

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
            int price, String redirectUrl) {
        System.out.println(redirectUrl);

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
        parameters.put("approval_url", redirectUrl); // 성공 시 redirect url - 추후에 프론트엔드 url로 변경
        parameters.put("cancel_url", redirectUrl); // 취소 시 redirect url
        parameters.put("fail_url", redirectUrl); // 실패 시 redirect url

        return parameters;
    }

    /**
     * KAKAO PAY 결제 승인을 하기위한 JSON BODY 생성
     * @param cid 가맹점 고유 코드 : 테스트 코드는 TC0ONETIME
     * @param request
     * @param userId
     * @return
     */
    public static Map<String, String> toApproveParam(String cid, PaymentRequestDTO.KakaoPaymentApproveRequestDTO request, String userId) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", request.tid());
        parameters.put("partner_order_id", request.partner_order_id());
        parameters.put("partner_user_id", userId);
        parameters.put("pg_token", request.pg_token());

        return parameters;
    }

    /**
     * KAKAO PAY 결제 취소를 하기위한 JSON BODY 생성
     * @param cid cid 가맹점 고유 코드 : 테스트 코드는 TC0ONETIME
     * @param request
     * @return
     */
    public static Map<String, String> toCancelParam(String cid, PaymentRequestDTO.KakaoPaymentCancelRequestDTO request) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", request.tid());
        parameters.put("cancel_amount", String.valueOf(request.cancel_amount()));
        parameters.put("cancel_tax_free_amount", String.valueOf(request.cancel_tax_free_amount()));
        parameters.put("cancel_vat_amount", String.valueOf(request.cancel_vat_amount()));
        //parameters.put("cancel_available_amount", request.cancel_available_amount());

        return parameters;
    }
}
