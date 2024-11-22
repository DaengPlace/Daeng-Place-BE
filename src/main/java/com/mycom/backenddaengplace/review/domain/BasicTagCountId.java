package com.mycom.backenddaengplace.review.domain;

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
public class BasicTagCountId implements Serializable {

    private Long basicTagId;
    private Long reviewId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicTagCountId that = (BasicTagCountId) o;
        return Objects.equals(basicTagId, that.basicTagId) &&
                Objects.equals(reviewId, that.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basicTagId, reviewId);
    }


}
