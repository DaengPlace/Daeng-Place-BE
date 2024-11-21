package com.mycom.backenddaengplace.common.domain.place.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "operation_hours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operationHourId;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    @Column(name = "start_time")
    private java.time.LocalDateTime startTime;

    @Column(name = "end_time")
    private java.time.LocalDateTime endTime;

    @Column(name = "is_day_off", nullable = false)
    private Boolean isDayOff;

    @Column(name = "break_start_time")
    private java.time.LocalDateTime breakStartTime;

    @Column(name = "break_end_time")
    private java.time.LocalDateTime breakEndTime;
}

