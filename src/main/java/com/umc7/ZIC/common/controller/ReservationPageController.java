package com.umc7.ZIC.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservation")
public class ReservationPageController {
    @GetMapping("/payment/kakao-pay")
    public String ready() {

        return "KakaoReady";
    }
}
