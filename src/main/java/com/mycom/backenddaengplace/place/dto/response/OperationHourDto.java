package com.mycom.backenddaengplace.place.dto.response;

import lombok.Data;

import java.time.LocalTime;

@Data
public class OperationHourDto {
    private Long placeId;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isDayOff;
}
