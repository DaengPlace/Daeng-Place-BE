package com.mycom.backenddaengplace.member.dto.response;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.member.enums.Gender;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailCheckResponse {

    private Boolean isValid;

    public static EmailCheckResponse from(Boolean isValid) {
        return EmailCheckResponse.builder()
                .isValid(isValid)
                .build();
    }
}
