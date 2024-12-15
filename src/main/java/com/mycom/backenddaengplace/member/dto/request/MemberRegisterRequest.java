package com.mycom.backenddaengplace.member.dto.request;

import com.mycom.backenddaengplace.member.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRegisterRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "이메일 주소를 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "별명을 입력해주세요.")
    @Size(min = 2, max = 20, message = "별명은 2자 이상 20자 이하로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "생년월일을 입력해주세요.")
    @Pattern(regexp = "\\d{8}", message = "생년월일은 YYYYMMDD 형식으로 입력해주세요.")
    private String birthDate;

    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;

    @NotNull(message = "시/도를 입력해주세요.")
    private String state;

    @NotNull(message = "시/군/구를 입력해주세요.")
    private String city;

    private String profileImageUrl;

    private Boolean locationStatus;

}
