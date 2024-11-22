package com.mycom.backenddaengplace.review.domain;

import com.mycom.backenddaengplace.common.domain.BaseEntity;
import com.mycom.backenddaengplace.pet.domain.Pet;
import com.mycom.backenddaengplace.trait.domain.TraitAnswer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "basic_tag_count")
public class BasicTagCount extends BaseEntity {

    @EmbeddedId
    private BasicTagCountId id;

    @MapsId("basicTagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basic_tag_id", nullable = false)
    private BasicTag basicTag;

    @MapsId("reviewId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

}
