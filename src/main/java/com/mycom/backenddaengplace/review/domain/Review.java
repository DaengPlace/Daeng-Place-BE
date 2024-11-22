package com.mycom.backenddaengplace.review.domain;

import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.place.domain.Place;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

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

    @Column(name = "trait_tag_count")
    private String traitTag;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "rating", precision = 2, nullable = false)
    private Double rating;



    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MediaFile> mediaFiles;

    @Builder
    public Review(
            Member member,
            Place place,
            String content,
            Double rating,
            String traitTag,
            List<BasicTagCount> basicTagCounts
    ) {
        this.member = member;
        this.place = place;
        this.content = content;
        this.rating = rating;
        this.traitTag = traitTag;
        this.basicTagCounts = basicTagCounts;

    }
}
