package com.mycom.backenddaengplace.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailSendCodeResponse {

    private String email;
    private String code;

    public static EmailSendCodeResponse from(String email, String code) {
        return EmailSendCodeResponse.builder()
                .email(email)
                .code(code)
                .build();
    }
}
