package com.mycom.backenddaengplace.place.dto.response;

import com.mycom.backenddaengplace.place.enums.Category;
import lombok.Data;

@Data
public class PlaceDto {
    private Long placeId;
    private String name;
    private String phone;
    private String description;
    private Category category;
    private String location;
    private Boolean is_parking;
    private Boolean inside;
    private Boolean outside;
    private int weight_limit;
    private int pet_fee;
    private Double latitude;
    private Double longitude;
    private Double rating;
    private Long review_count;
    private TodayOperationHourDto operationHour;
}
