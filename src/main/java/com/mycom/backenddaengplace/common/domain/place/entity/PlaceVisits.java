package com.mycom.backenddaengplace.common.domain.place.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "place_visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceVisits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitId;

    @Column(name = "id2", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "visited_at")
    private java.time.LocalDateTime visitedAt;
}
