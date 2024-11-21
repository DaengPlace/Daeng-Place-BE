package com.mycom.backenddaengplace.review.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "place_id", nullable = false)
    private Long placeId;
}
