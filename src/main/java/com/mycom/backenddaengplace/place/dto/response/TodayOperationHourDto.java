package com.mycom.backenddaengplace.place.dto.response;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TodayOperationHourDto {
    private Long placeId;
    private LocalTime todayOpen;
    private LocalTime todayClose;

}
