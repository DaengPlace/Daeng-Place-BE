package com.mycom.backenddaengplace.place.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "road_address", nullable = false)
    private String roadAddress;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "zipcode")
    private String zipcode;

    @OneToOne(mappedBy = "address")
    private Place place;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    private Location location;

    @Builder
    public Address(
            String roadAddress,
            String detailAddress,
            String zipcode
    ) {

        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.zipcode = zipcode;

    }


}
