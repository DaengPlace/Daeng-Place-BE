package com.mycom.backenddaengplace.auth.dto;

import com.mycom.backenddaengplace.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO; // 소셜 로그인 정보 저장
    private final Member member; // Member 객체와 연동

    public CustomOAuth2User(UserDTO userDTO, Member member) {
        this.userDTO = userDTO;
        this.member = member;
    }

    /**
     * OAuth2 사용자 속성 반환
     * - 현재는 attributes가 null로 반환되어 있으므로, 필요한 경우 이를 수정해야 합니다.
     */
    @Override
    public Map<String, Object> getAttributes() {
        // userDTO를 기반으로 속성을 반환하거나 필요한 데이터를 채울 수 있습니다.
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", userDTO.getEmail());
        attributes.put("nickname", userDTO.getNickname());
        attributes.put("provider", userDTO.getProvider());
        attributes.put("providerId", userDTO.getProviderId());
        attributes.put("profileImage", userDTO.getProfileImage());
        return attributes;
    }

    /**
     * 사용자 권한 반환
     * - 현재는 "ROLE_USER" 권한만 부여하도록 설정
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_USER"); // 단일 권한 ROLE_USER
    }

    /**
     * OAuth2 사용자의 이름 반환
     * - 닉네임이 존재하면 닉네임 반환, 없으면 "Unknown User" 반환
     */
    @Override
    public String getName() {
        return userDTO.getNickname() != null ? userDTO.getNickname() : "Unknown User";
    }

    /**
     * OAuth2 사용자의 고유 username 생성
     * - "provider_providerId" 형식으로 반환
     */
    public String getUsername() {
        return userDTO.getProvider() + "_" + userDTO.getProviderId();
    }

    /**
     * Member 엔티티 반환
     * - 소셜 로그인 이후 Member와 연동된 데이터를 반환
     */
    public Member getMember() {
        return member;
    }
    public String getProvider() {
        return userDTO.getProvider();
    }

    public String getProviderId() {
        return userDTO.getProviderId();
    }


}
