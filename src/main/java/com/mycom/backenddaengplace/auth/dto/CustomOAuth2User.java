package com.mycom.backenddaengplace.auth.dto;

import com.mycom.backenddaengplace.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final UserDTO userDTO;
    private final Member member;  // Member 추가

    public CustomOAuth2User(UserDTO userDTO, Member member) {  // 생성자 수정
        this.userDTO = userDTO;
        this.member = member;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(() -> "ROLE_USER");  // 람다식으로 단순화
        return collection;
    }

    @Override
    public String getName() {
        return userDTO.getNickname() != null ? userDTO.getNickname() : "Unknown User";
    }

    public String getUsername() {
        return userDTO.getProvider() + "_" + userDTO.getProviderId();
    }

    // Member 객체를 반환하는 메소드 추가
    public Member getMember() {
        return member;
    }
}