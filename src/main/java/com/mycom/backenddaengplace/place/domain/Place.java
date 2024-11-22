package com.mycom.backenddaengplace.place.domain;

import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.place.enums.WeatherType;
import com.mycom.backenddaengplace.review.domain.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "place")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "is_parking", nullable = false)
    private Boolean isParking;

    @Column(name = "weight_limit")
    private Integer weightLimit;

    @Column(name = "pet_fee")
    private Integer petFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "weather_type")
    private WeatherType weatherType;

    @OneToMany(mappedBy = "place")
    @Column(name = "reviews")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "place")
    @Column(name = "operation_hours")
    private List<OperationHour> operationHours = new ArrayList<>();

    @Builder
    Place(
            Address address,
            String name,
            String description,
            Category category,
            Boolean isParking,
            Integer weightLimit,
            Integer petFee,
            WeatherType weatherType
    ) {
        this.address = address;
        this.name = name;
        this.description = description;
        this.category = category;
        this.isParking = isParking;
        this.weightLimit = weightLimit;
        this.petFee = petFee;
        this.weatherType = weatherType;

    }
}