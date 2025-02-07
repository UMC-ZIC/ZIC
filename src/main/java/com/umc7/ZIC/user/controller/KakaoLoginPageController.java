package com.umc7.ZIC.user.controller;

import com.umc7.ZIC.apiPayload.exception.ApiResponse;
import com.umc7.ZIC.user.converter.UserConverter;
import com.umc7.ZIC.user.dto.KakaoUserInfoResponseDto;
import com.umc7.ZIC.user.dto.UserResponseDto;
import com.umc7.ZIC.user.service.KakaoService;
import com.umc7.ZIC.user.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
public class KakaoLoginPageController {

    @Value("${spring.security.oauth2.client.registration.kakao.client_id}")
    private String client_id;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect_uri}")
    private String redirect_uri;

//    private final UserService userService;

//    private final KakaoService kakaoService;

    
    // 테스트를 위함
    @GetMapping("/page")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        model.addAttribute("location", location);

        return "login";
    }

    @GetMapping("/user")
    public String getUser(@AuthenticationPrincipal OAuth2User principal) {
        log.info(principal.getAuthorities().toString());
        return principal.getAuthorities().toString();
    }
    @GetMapping("/home")
    public String home() {

        return "home";
    }

//    @GetMapping("/login/oauth2")
//    public ApiResponse<UserResponseDto.userDetailsDto> oauth2(@RequestParam("code") String code, @RequestParam("state") String state) {
//        log.info("Wwwwwwwwwwwwww");
//        String kakaoAccessToken = kakaoService.getAccessTokenFromKakao(code);
//        KakaoUserInfoResponseDto kakaoUserInfo = kakaoService.getUserInfo(kakaoAccessToken);
//        UserResponseDto.userDetailsDto userDetailsDto = userService.kaKaoGetUser(kakaoUserInfo);
//        log.info(userDetailsDto.toString());
//        return ApiResponse.onSuccess(userDetailsDto);
//    }

    @GetMapping("/success")
    public String success() {

        return "success";
    }
}
