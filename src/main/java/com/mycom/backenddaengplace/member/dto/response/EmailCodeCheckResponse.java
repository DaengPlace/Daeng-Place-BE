package com.mycom.backenddaengplace.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailCodeCheckResponse {

    private Boolean success;

    public static EmailCodeCheckResponse from(Boolean success) {
        return EmailCodeCheckResponse.builder().success(success).build();
    }

}
