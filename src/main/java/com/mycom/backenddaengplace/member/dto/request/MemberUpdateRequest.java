package com.mycom.backenddaengplace.member.dto.request;

import com.mycom.backenddaengplace.member.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberUpdateRequest {

    @NotBlank(message = "별명을 입력해주세요.")
    @Size(min = 2, max = 20, message = "별명은 2자 이상 20자 이하로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "생년월일을 입력해주세요.")
    @Pattern(regexp = "\\d{6}", message = "생년월일은 YYMMDD 형식으로 입력해주세요.")
    private String birthDate;

    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;

    @NotNull(message = "시/도를 입력해주세요.")
    private String state;

    @NotNull(message = "시/군/구를 입력해주세요.")
    private String city;

    private String profileImageUrl;

}
