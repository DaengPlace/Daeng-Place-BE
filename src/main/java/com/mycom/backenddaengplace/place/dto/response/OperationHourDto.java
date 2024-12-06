package com.mycom.backenddaengplace.place.dto.response;

import lombok.Data;

import java.time.LocalTime;

@Data
public class OperationHourDto {
    private Long placeId;
    private LocalTime mondayOpen;
    private LocalTime mondayClose;
    private LocalTime tuesdayOpen;
    private LocalTime tuesdayClose;
    private LocalTime wednesdayOpen;
    private LocalTime wednesdayClose;
    private LocalTime thursdayOpen;
    private LocalTime thursdayClose;
    private LocalTime fridayOpen;
    private LocalTime fridayClose;
    private LocalTime saturdayOpen;
    private LocalTime saturdayClose;
    private LocalTime sundayOpen;
    private LocalTime sundayClose;

}
