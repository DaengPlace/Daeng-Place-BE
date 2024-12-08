package com.mycom.backenddaengplace.auth.service;

import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.auth.dto.UserDTO;
import com.mycom.backenddaengplace.auth.repository.AuthMemberRepository;
import com.mycom.backenddaengplace.auth.dto.response.GoogleResponse;
import com.mycom.backenddaengplace.auth.dto.response.KakaoResponse;
import com.mycom.backenddaengplace.auth.dto.response.OAuth2Response;
import com.mycom.backenddaengplace.member.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthMemberRepository authMemberRepository;
    private final AuthMemberService authMemberService;

    public CustomOAuth2UserService(AuthMemberRepository authMemberRepository, AuthMemberService authMemberService) {
        this.authMemberRepository = authMemberRepository;
        this.authMemberService = authMemberService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // 소셜 로그인 유저 데이터 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 제공자 구분
        OAuth2Response oAuth2Response = getOAuth2Response(registrationId, oAuth2User); // OAuth2Response로 매핑

        // **회원 정보 저장 또는 업데이트**
        Member member = saveOrUpdateUser(oAuth2Response);

        // **UserDTO 생성**
        UserDTO userDTO = createUserDTO(member);

        // **CustomOAuth2User 반환**
        return new CustomOAuth2User(userDTO, member);
    }

    /**
     * 제공자별 OAuth2Response 매핑 로직
     */
    private OAuth2Response getOAuth2Response(String registrationId, OAuth2User oAuth2User) {
        if ("kakao".equals(registrationId)) {
            return new KakaoResponse(oAuth2User.getAttributes());
        } else if ("google".equals(registrationId)) {
            return new GoogleResponse(oAuth2User.getAttributes());
        } else {
            log.error("Unsupported provider: {}", registrationId);
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }
    }

    /**
     * 회원 정보를 저장하거나 업데이트
     */
    private Member saveOrUpdateUser(OAuth2Response oAuth2Response) {
        String provider = oAuth2Response.getProvider();
        String providerId = oAuth2Response.getProviderId();
        String email = oAuth2Response.getEmail();

        // **기본 이메일 처리**
        if (email == null || email.isEmpty()) {
            email = provider + "-" + providerId + "@noemail.com";
        }

        log.info("Social login request: provider={}, providerId={}, email={}", provider, providerId, email);

        // **DB에서 회원 조회**
        Member member = authMemberRepository.findByProviderAndProviderId(provider, providerId);

        if (member == null) {
            // **새 사용자 저장**
            member = Member.builder()
                    .provider(provider)
                    .providerId(providerId)
                    .email(email)
                    .nickname(oAuth2Response.getName())
                    .profileImageUrl(oAuth2Response.getProfileImage())
                    .build();
            authMemberRepository.save(member);
            log.info("New user registered: {}", member);
        } else {
            // **기존 사용자 업데이트**
            authMemberService.updateMember(member,
                    email,
                    oAuth2Response.getName(),
                    oAuth2Response.getProfileImage(),
                    provider,
                    providerId);
            log.info("Existing user updated: {}", member);
        }

        return member;
    }

    /**
     * UserDTO 객체 생성
     */
    private UserDTO createUserDTO(Member member) {
        UserDTO userDTO = new UserDTO();
        userDTO.setProvider(member.getProvider());
        userDTO.setProviderId(member.getProviderId());
        userDTO.setNickname(member.getNickname());
        userDTO.setProfileImage(member.getProfileImageUrl());
        userDTO.setEmail(member.getEmail());
        return userDTO;
    }
}
