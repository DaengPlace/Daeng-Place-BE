package com.mycom.backenddaengplace.favorite.domain;

import com.mycom.backenddaengplace.common.domain.BaseEntity;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.place.domain.Place;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "favorite")
@NoArgsConstructor
public class Favorite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Builder
    public Favorite(
            Member member,
            Place place
    ) {
        this.member = member;
        this.place = place;
    }

}
