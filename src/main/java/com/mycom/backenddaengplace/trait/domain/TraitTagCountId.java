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
public class TraitTagCountId implements Serializable {

    private Long traitTagId;
    private Long reviewId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraitTagCountId that = (TraitTagCountId) o;
        return Objects.equals(traitTagId, that.traitTagId) &&
                Objects.equals(reviewId, that.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traitTagId, reviewId);
    }


}
