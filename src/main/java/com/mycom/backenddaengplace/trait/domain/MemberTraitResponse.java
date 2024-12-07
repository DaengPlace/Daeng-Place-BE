package com.mycom.backenddaengplace.trait.domain;

import com.mycom.backenddaengplace.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTraitResponse {

    @EmbeddedId
    private MemberTraitResponseId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("traitAnswerId")
    @JoinColumn(name = "trait_answer_id", nullable = false)
    private TraitAnswer traitAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("traitQuestionId")
    @JoinColumn(name = "trait_question_id", nullable = false)
    private TraitQuestion traitQuestion;

    @Builder
    public MemberTraitResponse(MemberTraitResponseId id, Member member, TraitAnswer traitAnswer, TraitQuestion traitQuestion) {
        this.id = id;
        this.member = member;
        this.traitAnswer = traitAnswer;
        this.traitQuestion = traitQuestion;
    }
}
