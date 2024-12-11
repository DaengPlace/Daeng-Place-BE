package com.mycom.backenddaengplace.auth.dto.response;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
@Slf4j
public class GoogleResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {

        this.attribute = attribute;
    }

    @Override
    public String getProvider() {

        return "google";
    }

    @Override
    public String getProviderId() {
        String providerId = attribute.get("sub").toString();
        log.info("GoogleResponse - Provider ID: {}", providerId); // 로그 추가
        return providerId;
    }

    @Override
    public String getEmail() {
        String email = attribute.get("email").toString();
        log.info("GoogleResponse - Email: {}", email); // 로그 추가
        return email;
    }

    @Override
    public String getName() {
        String name = attribute.get("name").toString();
        log.info("GoogleResponse - Name: {}", name); // 로그 추가
        return name;
    }

    @Override
    public String getProfileImage() {
        // Google의 프로필 이미지 URL 키에 따라 값을 반환합니다.
        return attribute.containsKey("picture") ? attribute.get("picture").toString() : null;
    }
}