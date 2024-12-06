package com.mycom.backenddaengplace.place.domain;

import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.place.enums.OperationStatus;
import com.mycom.backenddaengplace.review.domain.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "place")
@Getter
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

    @Column(name = "phone")
    private String phone;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "is_parking")
    private Boolean isParking;

    @Column(name = "inside")
    private Boolean inside;

    @Column(name = "outside")
    private Boolean outside;

    @Column(name = "pet_fee")
    private Integer petFee;

    @Column(name = "weight_limit")
    private Integer weightLimit;

    @Column(name = "homepage")
    private String homepage;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_status")
    private OperationStatus operationStatus;

    @OneToMany(mappedBy = "place")
    @Column(name = "reviews")
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "place")
    private OperationHour operationHour;

    @Builder
    Place(
            Address address,
            String name,
            String phone,
            String description,
            Category category,
            Boolean isParking,
            Boolean inside,
            Boolean outside,
            String homepage,
            OperationStatus operationStatus,
            Integer petFee,
            Integer weightLimit
    ) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.category = category;
        this.isParking = isParking;
        this.inside = inside;
        this.outside = outside;
        this.homepage = homepage;
        this.operationStatus = operationStatus;
        this.petFee = petFee;
        this.weightLimit = weightLimit;
    }
}