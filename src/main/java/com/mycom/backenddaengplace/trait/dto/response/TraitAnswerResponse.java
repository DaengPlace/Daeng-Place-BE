package com.mycom.backenddaengplace.trait.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TraitAnswerResponse {

    private Long answerId;
    private String content;
}
