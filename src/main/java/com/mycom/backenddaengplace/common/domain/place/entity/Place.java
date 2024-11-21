package com.mycom.backenddaengplace.common.domain.place.entity;

import com.mycom.backenddaengplace.common.domain.place.entity.enums.Category;
import com.mycom.backenddaengplace.common.domain.place.entity.enums.WeatherType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "place")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    @ManyToOne
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

    @Enumerated(EnumType.STRING)
    @Column(name = "weather_type")
    private WeatherType weatherType;
}