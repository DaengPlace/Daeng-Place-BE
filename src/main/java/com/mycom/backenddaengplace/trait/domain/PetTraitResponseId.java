package com.mycom.backenddaengplace.trait.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PetTraitResponseId implements Serializable {

    private Long petId;
    private Long traitAnswerId;
    private Long traitQuestionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetTraitResponseId that = (PetTraitResponseId) o;
        return petId.equals(that.petId) &&
                traitAnswerId.equals(that.traitAnswerId) &&
                traitQuestionId.equals(that.traitQuestionId);

    }

    @Override
    public int hashCode() {
            return petId.hashCode() + traitAnswerId.hashCode() + traitQuestionId.hashCode();
    }


}
