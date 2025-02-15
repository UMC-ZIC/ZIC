package com.umc7.ZIC.security.OAuth;

import com.umc7.ZIC.user.domain.User;
import com.umc7.ZIC.user.domain.enums.RoleType;
import com.umc7.ZIC.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User;
        try {
            oAuth2User = super.loadUser(userRequest);
            log.info("loadUser 메서드 호출됨");
        } catch (OAuth2AuthenticationException e) {
            log.error("OAuth2 인증 오류: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("예기치 않은 오류 발생: {}", e.getMessage());
            throw new OAuth2AuthenticationException(new OAuth2Error("unknown_error", "알 수 없는 오류가 발생했습니다.", null), e);
        }

        Map<String, Object> attributes = oAuth2User.getAttributes();
        Long kakaoId;
        try {
            kakaoId = (Long) attributes.get("id");
        } catch (NullPointerException e) {
            log.error("NullPointerException 발생: {}", e.getMessage());
            throw new OAuth2AuthenticationException(new OAuth2Error("missing_kakao_account", "카카오 계정 정보를 찾을 수 없습니다.", null), e);
        }

        // 카카오 계정에서 이메일 추출
        Map<String, Object> kakaoAccount;
        String email;
        String nickname;
        Map<String, Object> profile;
//        String profileImageUrl;
        String profileTumbnailImageUrl;

        try {
            kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            profile = (Map<String, Object>) kakaoAccount.get("profile");

            email = (String) kakaoAccount.get("email");
            nickname = (String) profile.get("nickname");
//            profileImageUrl = (String) profile.get("profile_image_url");
            profileTumbnailImageUrl = (String) profile.get("thumbnail_image_url");
            if (email == null) {
                throw new OAuth2AuthenticationException(new OAuth2Error("missing_email", "이메일을 찾을 수 없습니다.", null));
            }
        } catch (NullPointerException e) {
            log.error("NullPointerException 발생: {}", e.getMessage());
            throw new OAuth2AuthenticationException(new OAuth2Error("missing_kakao_account", "카카오 계정 정보를 찾을 수 없습니다.", null), e);
        }
        log.info(nickname);
        // 카카오 아이디로 User 조회 및 가입.
        User user = saveOrUpdateUser(kakaoId, email, nickname, profileTumbnailImageUrl);


        // 이메일을 Principal로 사용하기 위해 attributes 수정
        Map<String, Object> modifiedAttributes = new HashMap<>(attributes);
        modifiedAttributes.put("email", email);
        modifiedAttributes.put("nickname", nickname); // 닉네임 추가

        // DefaultOAuth2User 대신 CustomOAuth2User 객체 반환
        return new CustomOAuth2User(
                oAuth2User.getAuthorities(),
                modifiedAttributes,
                "email", // email Principal로 설정
                user // User 객체 추가
        );

    }

    private User saveOrUpdateUser(Long kakaoId, String email, String nickname, String profileImageUrl) {
        return userRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> {
                    // 새로운 사용자 등록
                    User newUser = User.builder()
                            .name(nickname) // 임시 이름
                            .kakaoId(kakaoId)
                            .email(email)
                            .role(RoleType.PENDING) // 기본 역할을 PENDING으로 설정
                            .profilePic(profileImageUrl) //필요시 null 일 경우 기본 프로필 사진 url 저장
                            .build();

                    return userRepository.save(newUser);
                });
    }
}