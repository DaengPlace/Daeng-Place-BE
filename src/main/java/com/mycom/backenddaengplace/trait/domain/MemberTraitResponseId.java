package com.mycom.backenddaengplace.trait.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MemberTraitResponseId implements Serializable {

    private Long memberId;
    private Long traitAnswerId;
    private Long traitQuestionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberTraitResponseId that = (MemberTraitResponseId) o;
        return memberId.equals(that.memberId) &&
                traitAnswerId.equals(that.traitAnswerId) &&
                traitQuestionId.equals(that.traitQuestionId);
    }

    @Override
    public int hashCode() {
        return memberId.hashCode() + traitAnswerId.hashCode() + traitQuestionId.hashCode();
    }
}
