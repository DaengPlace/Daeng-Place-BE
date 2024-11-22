package com.mycom.backenddaengplace.place.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlaceDetailResponse {
    private Long placeId;
    private String name;
    private String description;
    private String category;
    private String location;
    private String start_time;
    private String close_time;
    private String holiday;
    private Boolean is_parking;
    private String weather_type;
    private int weight_limit;
    private int pet_fee;
//    private float rating;  // 추가 예정
}
