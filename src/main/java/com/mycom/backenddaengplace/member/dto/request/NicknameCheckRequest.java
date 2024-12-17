package com.mycom.backenddaengplace.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NicknameCheckRequest {

    @NotBlank(message = "별명을 입력해주세요.")
    @Size(min = 2, max = 20, message = "별명은 2자 이상 20자 이하로 입력해주세요.")
    private String nickname;
}
