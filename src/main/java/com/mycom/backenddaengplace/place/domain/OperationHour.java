package com.mycom.backenddaengplace.place.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "operation_hour")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OperationHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="operation_hour_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "is_day_off", nullable = false)
    private Boolean isDayOff;

    @Builder
    public OperationHour(
            Place place,
            String dayOfWeek,
            LocalDateTime startTime,
            LocalDateTime endTime,
            boolean isDayOff
    ){
        this.place = place;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isDayOff = isDayOff;

    }

}

