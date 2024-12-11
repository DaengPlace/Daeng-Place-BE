package com.mycom.backenddaengplace.trait.dto.response;

import com.mycom.backenddaengplace.trait.domain.TraitTagCount;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TraitTagCountResponse {
    private Long traitTagId;
    private String content;

    public static TraitTagCountResponse from(TraitTagCount traitTagCount) {
        return TraitTagCountResponse.builder()
                .traitTagId(traitTagCount.getTraitTag().getId())
                .content(traitTagCount.getTraitTag().getContent())
                .build();
    }
}