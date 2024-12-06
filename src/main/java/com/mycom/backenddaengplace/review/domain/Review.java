package com.mycom.backenddaengplace.review.domain;

import com.mycom.backenddaengplace.common.domain.BaseEntity;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.place.domain.Place;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @OneToMany(mappedBy = "review")
    @Column(name = "review_tag_count")
    private List<BasicTagCount> basicTagCounts = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "review_trait_tags",
            joinColumns = @JoinColumn(name = "review_id"))
    private List<String> traitTags;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "rating", precision = 2, nullable = false)
    private Double rating;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFile> mediaFiles;

    @Builder
    public Review(
            Member member,
            Place place,
            String content,
            Double rating,
            List<String> traitTags,
            List<BasicTagCount> basicTagCounts
    ) {
        this.member = member;
        this.place = place;
        this.content = content;
        this.rating = rating;
        this.traitTags = traitTags;
        this.basicTagCounts = basicTagCounts;

    }

    public void update(String newContent, Double newRating, List<String> newTraitTags) {
        if (newContent != null && !newContent.isBlank()) {
            this.content = newContent;
        }
        if (newRating != null && newRating >= 0.0 && newRating <= 5.0) {
            this.rating = newRating;
        }
        if (newTraitTags != null) {
            this.traitTags = newTraitTags;
        }
    }
}
