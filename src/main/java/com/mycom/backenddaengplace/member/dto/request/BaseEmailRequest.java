package com.mycom.backenddaengplace.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEmailRequest {

    @NotEmpty(message = "이메일은 입력해주세요.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

}
