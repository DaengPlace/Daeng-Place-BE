package com.mycom.backenddaengplace.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailDuplicateCheckResponse {

    private Boolean isValid;

    public static EmailDuplicateCheckResponse from(Boolean isValid) {
        return EmailDuplicateCheckResponse.builder()
                .isValid(isValid)
                .build();
    }
}
