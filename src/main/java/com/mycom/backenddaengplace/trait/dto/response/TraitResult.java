package com.mycom.backenddaengplace.trait.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraitResult {
    private String traitQuestion;
    private String traitAnswer;
}
