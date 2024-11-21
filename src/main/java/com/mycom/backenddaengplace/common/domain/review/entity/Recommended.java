package com.mycom.backenddaengplace.common.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommended")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recommended {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommended_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount;

    @Column(name = "avg_rating", precision = 2, scale = 1, nullable = false)
    private Double avgRating;
}
