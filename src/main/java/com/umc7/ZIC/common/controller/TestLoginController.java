package com.umc7.ZIC.common.controller;


import com.umc7.ZIC.apiPayload.code.status.ErrorStatus;
import com.umc7.ZIC.apiPayload.exception.handler.UserHandler;
import com.umc7.ZIC.security.JwtTokenProvider;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestLoginController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    //test용
    @Operation(summary = "테스트용 대여자 JWT 토큰 생성",
            description = "1번은 유저토큰, 2번은 대여자 토큰입니다." +
                    "<br>get-token-from-user-id와 똑같지만 대여자 이용자 구분하기위해 일부러 2개 만들었습니다.")
    @Parameters({
            @Parameter(name = "userId", description = "<b>1</b> : 유저 토큰" +
                    "<br><b>2</b> : 대여자 토큰"),
    })
    @GetMapping("/owner/get-token-from-user-id")
    public String testCreateOwnerToken(@RequestParam Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        String userToken =jwtTokenProvider.createAccessToken(userId, user.getRole().toString(), user.getName());
        return userToken;
    }

    //test용
    //테스트용으로 일단 프론트에서 endpoint 구분하기 위해 다르게 했는데 위와 중복된 코드
    @Operation(summary = "테스트용 유저 JWT 토큰 생성",
            description = "1번은 유저토큰, 2번은 대여자 토큰입니다.")
    @Parameters({
            @Parameter(name = "userId", description = "<b>1</b> : 유저 토큰" +
                    "<br><b>2</b> : 대여자 토큰"),
    })
    @GetMapping("/user/get-token-from-user-id")
    public String testCreateUserToken(@RequestParam Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        String userToken =jwtTokenProvider.createAccessToken(userId, user.getRole().toString(), user.getName());
        return userToken;
    }
}
