package com.mycom.backenddaengplace.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DuplicateCheckResponse {

    private Boolean isValid;

    public static DuplicateCheckResponse from(Boolean isValid) {
        return DuplicateCheckResponse.builder()
                .isValid(isValid)
                .build();
    }
}
