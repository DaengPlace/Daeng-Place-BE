package com.mycom.backenddaengplace.pet.dto.request;

import com.mycom.backenddaengplace.member.enums.Gender;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasePetRequest {
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
    private String name;

    @NotNull(message = "견종을 선택해주세요.")
    private Long breedTypeId;  // Long 타입이므로 @NotNull 사용

    @NotBlank(message = "생년월일을 입력해주세요.")
    @Pattern(regexp = "\\d{8}", message = "생년월일은 YYYYMMDD 형식으로 입력해주세요.")
    private String birthDate;

    @NotNull(message = "몸무게를 입력해주세요.")
    @DecimalMin(value = "0.1", message = "몸무게는 0.1kg 이상이어야 합니다.")
    @Digits(integer = 3, fraction = 1, message = "몸무게는 소수점 첫째자리까지 입력 가능합니다.")
    private Double weight;

    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;

    @NotNull(message = "중성화 여부를 선택해주세요.")
    private Boolean isNeutered;
}
