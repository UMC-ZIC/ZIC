package com.umc7.ZIC.common.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kakao-login")
public class KakaoLoginPageController {

    @Value("${spring.security.oauth2.client.registration.kakao.client_id}")
    private String client_id;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect_uri}")
    private String redirect_uri;

    
    // 테스트를 위함
    @GetMapping("/page")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        model.addAttribute("location", location);

        return "login";
    }

    @GetMapping("/home")
    public String home() {

        return "home";
    }

    @GetMapping("/success")
    public String success() {

        return "success";
    }
}
