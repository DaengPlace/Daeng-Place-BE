package com.mycom.backenddaengplace.place.domain;

import com.mycom.backenddaengplace.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "place_visit")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceVisit {

    @EmbeddedId
    private PlaceVisitId id;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @MapsId("placeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "visited_at")
    private LocalDateTime visitedAt;

    @Builder
    public PlaceVisit(
            Place place,
            LocalDateTime visitedAt
    ) {
        this.place = place;
        this.visitedAt = visitedAt;

    }
}
