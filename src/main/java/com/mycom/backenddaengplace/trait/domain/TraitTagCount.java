package com.mycom.backenddaengplace.trait.domain;

import com.mycom.backenddaengplace.common.domain.BaseEntity;
import com.mycom.backenddaengplace.review.domain.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "trait_tag_count")
public class TraitTagCount extends BaseEntity {

    @EmbeddedId
    private TraitTagCountId id;

    @MapsId("traitTagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trait_tag_id", nullable = false)
    private TraitTag traitTag;

    @MapsId("reviewId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

}
