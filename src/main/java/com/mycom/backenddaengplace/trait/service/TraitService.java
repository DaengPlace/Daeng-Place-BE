package com.mycom.backenddaengplace.trait.service;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.pet.domain.Pet;
import com.mycom.backenddaengplace.pet.repository.PetRepository;
import com.mycom.backenddaengplace.trait.dto.response.*;
import com.mycom.backenddaengplace.trait.enums.QuestionTarget;
import com.mycom.backenddaengplace.trait.repository.MemberTraitResponseRepository;
import com.mycom.backenddaengplace.trait.repository.PetTraitResponseRepository;
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
    private final PetRepository petRepository;
    private final MemberTraitResponseRepository memberTraitResponseRepository;
    private final PetTraitResponseRepository petTraitResponseRepository;

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

    public TraitResultResponse getTraitResult(Member member) {

        List<Pet> pets = petRepository.findByMemberId(member.getId());

        List<TraitResult> memberTraits = memberTraitResponseRepository.findByMemberId(member.getId()).stream()
                .map(traitResponse -> TraitResult.builder()
                        .traitQuestion(traitResponse.getTraitQuestion().getContent())
                        .traitAnswer(traitResponse.getTraitAnswer().getContent())
                        .build())
                .toList();

        List<PetTraitResult> petTraitResults = pets.stream()
                .map(pet -> {
                    List<TraitResult> petTraits = petTraitResponseRepository.findByPetId(pet.getId()).stream()
                            .map(petTrait -> TraitResult.builder()
                                    .traitQuestion(petTrait.getTraitQuestion().getContent())
                                    .traitAnswer(petTrait.getTraitAnswer().getContent())
                                    .build())
                            .toList();

                    return PetTraitResult.builder()
                            .petId(pet.getId())
                            .petName(pet.getName())
                            .petTraits(petTraits)
                            .build();
                })
                .toList();


        return TraitResultResponse.builder()
                .memberTraits(memberTraits)
                .petTraits(petTraitResults)
                .build();
    }
}
