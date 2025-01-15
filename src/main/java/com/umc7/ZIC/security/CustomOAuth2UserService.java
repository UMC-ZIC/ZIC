package com.umc7.ZIC.security;

import com.umc7.ZIC.common.repository.RegionRepository;
import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.domain.enums.RoleType;
import com.umc7.ZIC.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegionRepository regionRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("loadUser 메서드 호출됨");

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Long kakaoId = (Long) attributes.get("id");

        // 카카오 계정에서 이메일 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        // 카카오 아이디로 User 조회 및 가입.
        User user = saveOrUpdateUser(kakaoId, email);

        // JWT 토큰 생성
        String jwtToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole().name());

        // 이메일을 Principal로 사용하기 위해 attributes 수정
        Map<String, Object> modifiedAttributes = new HashMap<>(attributes);
        modifiedAttributes.put("email", email);
        modifiedAttributes.put("jwtToken", jwtToken); // JWT 토큰 추가

        // DefaultOAuth2User 대신 CustomOAuth2User 객체 반환
        return new CustomOAuth2User(
                oAuth2User.getAuthorities(),
                modifiedAttributes,
                "email", // email Principal로 설정
                user // User 객체 추가
        );
    }

    private User saveOrUpdateUser(Long kakaoId, String email) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    // 새로운 사용자 등록
                    User newUser = User.builder()
                            .name("임시이름") // 임시 이름
                            .kakaoId(kakaoId)
                            .email(email)
                            .role(RoleType.PENDING) // 기본 역할을 PENDING으로 설정
                            .build();

                    return userRepository.save(newUser);
                });
    }
}