package com.mycom.backenddaengplace.auth.dto.response;

import java.util.Map;

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

        return attribute.get("sub").toString();
    }

    @Override
    public String getEmail() {

        return attribute.get("email").toString();
    }

    @Override
    public String getName() {

        return attribute.get("name").toString();
    }
    @Override
    public String getProfileImage() {
        // Google의 프로필 이미지 URL 키에 따라 값을 반환합니다.
        return attribute.containsKey("picture") ? attribute.get("picture").toString() : null;
    }
}