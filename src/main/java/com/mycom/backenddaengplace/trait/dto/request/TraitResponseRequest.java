package com.mycom.backenddaengplace.trait.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraitResponseRequest {
    private Long traitQuestionId;
    private Long traitAnswerId;
}
