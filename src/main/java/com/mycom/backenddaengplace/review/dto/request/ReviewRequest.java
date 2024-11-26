package com.mycom.backenddaengplace.review.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewRequest {
    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId;

    @NotNull(message = "별점은 필수입니다.")
    @DecimalMin(value = "0.0", message = "별점은 0.0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "별점은 5.0 이하여야 합니다.")
    private Double rating;

    @NotBlank(message = "리뷰 내용은 필수입니다.")
    @Size(max = 500, message = "리뷰 내용은 500자를 초과할 수 없습니다.")
    private String content;

    private String traitTag;
}
