package com.mycom.backenddaengplace.place.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "latitude", precision = 10)
    private Double latitude;

    @Column(name = "longitude", precision = 11)
    private Double longitude;

    @Builder
    public Location(
            Address address,
            Double latitude,
            Double longitude
    ) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}