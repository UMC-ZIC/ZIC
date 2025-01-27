package com.umc7.ZIC.common.controller;


import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.domain.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestLoginController {

    private final JwtTokenProvider jwtTokenProvider;

    //test용
    @GetMapping("/get-token-from-user-id")
    public String testCreateOwnerToken(@RequestParam Long userId){
        String userToken =jwtTokenProvider.createRefreshToken(userId, RoleType.OWNER.name());
        return userToken;
    }

    //test용
    @GetMapping("/get-token-from-user-id")
    public String testCreateUserToken(@RequestParam Long userId){
        String userToken =jwtTokenProvider.createRefreshToken(userId, RoleType.USER.name());
        return userToken;
    }
}
