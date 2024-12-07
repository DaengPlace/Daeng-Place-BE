package com.mycom.backenddaengplace.trait.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TraitQuestionResponse {

    private Long questionId;
    private String content;
    private List<TraitAnswerResponse> answers;
}
