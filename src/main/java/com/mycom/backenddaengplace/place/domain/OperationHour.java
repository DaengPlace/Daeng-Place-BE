package com.mycom.backenddaengplace.place.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "operation_hour")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OperationHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_hour_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "monday_open")
    private LocalTime mondayOpen;

    @Column(name = "monday_close")
    private LocalTime mondayClose;

    @Column(name = "tuesday_open")
    private LocalTime tuesdayOpen;

    @Column(name = "tuesday_close")
    private LocalTime tuesdayClose;

    @Column(name = "wednesday_open")
    private LocalTime wednesdayOpen;

    @Column(name = "wednesday_close")
    private LocalTime wednesdayClose;

    @Column(name = "thursday_open")
    private LocalTime thursdayOpen;

    @Column(name = "thursday_close")
    private LocalTime thursdayClose;

    @Column(name = "friday_open")
    private LocalTime fridayOpen;

    @Column(name = "friday_close")
    private LocalTime fridayClose;

    @Column(name = "saturday_open")
    private LocalTime saturdayOpen;

    @Column(name = "saturday_close")
    private LocalTime saturdayClose;

    @Column(name = "sunday_open")
    private LocalTime sundayOpen;

    @Column(name = "sunday_close")
    private LocalTime sundayClose;

    @Builder
    public OperationHour(
            Place place,
            LocalTime mondayOpen,
            LocalTime mondayClose,
            LocalTime tuesdayOpen,
            LocalTime tuesdayClose,
            LocalTime wednesdayOpen,
            LocalTime wednesdayClose,
            LocalTime thursdayOpen,
            LocalTime thursdayClose,
            LocalTime fridayOpen,
            LocalTime fridayClose,
            LocalTime saturdayOpen,
            LocalTime saturdayClose,
            LocalTime sundayOpen,
            LocalTime sundayClose
    ) {
        this.place = place;
        this.mondayOpen = mondayOpen;
        this.mondayClose = mondayClose;
        this.tuesdayOpen = tuesdayOpen;
        this.tuesdayClose = tuesdayClose;
        this.wednesdayOpen = wednesdayOpen;
        this.wednesdayClose = wednesdayClose;
        this.thursdayOpen = thursdayOpen;
        this.thursdayClose = thursdayClose;
        this.fridayOpen = fridayOpen;
        this.fridayClose = fridayClose;
        this.saturdayOpen = saturdayOpen;
        this.saturdayClose = saturdayClose;
        this.sundayOpen = sundayOpen;
        this.sundayClose = sundayClose;
    }

}

