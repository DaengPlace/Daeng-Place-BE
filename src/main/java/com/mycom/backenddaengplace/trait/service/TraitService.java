package com.mycom.backenddaengplace.trait.service;

import com.mycom.backenddaengplace.trait.dto.response.TraitAnswerResponse;
import com.mycom.backenddaengplace.trait.dto.response.TraitQuestionResponse;
import com.mycom.backenddaengplace.trait.enums.QuestionTarget;
import com.mycom.backenddaengplace.trait.repository.TraitQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TraitService {

    private final TraitQuestionRepository traitQuestionRepository;

    public List<TraitQuestionResponse> getTraitQuestions(QuestionTarget target) {
        return traitQuestionRepository.findByTarget(target).stream()
                .map(question -> TraitQuestionResponse.builder()
                        .questionId(question.getId())
                        .content(question.getContent())
                        .answers(
                                question.getTraitAnswers().stream()
                                        .map(answer -> TraitAnswerResponse.builder()
                                                .answerId(answer.getId())
                                                .content(answer.getContent())
                                                .build()
                                        )
                                        .collect(Collectors.toList())
                        )
                        .build()
                )
                .collect(Collectors.toList());
    }
}
