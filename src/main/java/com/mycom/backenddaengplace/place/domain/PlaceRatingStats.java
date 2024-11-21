package com.mycom.backenddaengplace.place.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "place_rating_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRatingStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_rating_stats_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "rating", precision = 2, scale = 1)
    private Double rating;
}