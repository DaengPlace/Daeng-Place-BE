package com.mycom.backenddaengplace.trait.service;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.trait.domain.MemberTraitResponse;
import com.mycom.backenddaengplace.trait.domain.MemberTraitResponseId;
import com.mycom.backenddaengplace.trait.domain.TraitAnswer;
import com.mycom.backenddaengplace.trait.domain.TraitQuestion;
import com.mycom.backenddaengplace.trait.dto.request.TraitResponseRequest;
import com.mycom.backenddaengplace.trait.dto.request.MemberTraitResponseRequestList;
import com.mycom.backenddaengplace.trait.exception.TraitAnswerNotFoundException;
import com.mycom.backenddaengplace.trait.exception.TraitQuestionNotFoundException;
import com.mycom.backenddaengplace.trait.repository.MemberTraitResponseRepository;
import com.mycom.backenddaengplace.trait.repository.TraitAnswerRepository;
import com.mycom.backenddaengplace.trait.repository.TraitQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberTraitService {

    private final TraitAnswerRepository traitAnswerRepository;
    private final TraitQuestionRepository traitQuestionRepository;
    private final MemberTraitResponseRepository memberTraitResponseRepository;

    public void saveMemberTraitResponse(MemberTraitResponseRequestList requestList, Member member) {
        for (TraitResponseRequest request : requestList.getMemberTraitResponseRequestList()) {

            TraitQuestion traitQuestion = traitQuestionRepository.findById(request.getTraitQuestionId())
                    .orElseThrow(() -> new TraitQuestionNotFoundException(request.getTraitQuestionId()));

            TraitAnswer traitAnswer = traitAnswerRepository.findById(request.getTraitAnswerId())
                    .orElseThrow(() -> new TraitAnswerNotFoundException(request.getTraitAnswerId()));

            // 복합키 생성
            MemberTraitResponseId responseId = new MemberTraitResponseId(
                    member.getId(),
                    request.getTraitAnswerId(),
                    request.getTraitQuestionId()
            );

            MemberTraitResponse response = MemberTraitResponse.builder()
                    .id(responseId)
                    .member(member)
                    .traitQuestion(traitQuestion)
                    .traitAnswer(traitAnswer)
                    .build();

            memberTraitResponseRepository.save(response);
        }
    }

}
