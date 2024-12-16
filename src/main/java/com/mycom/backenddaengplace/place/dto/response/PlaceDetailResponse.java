package com.mycom.backenddaengplace.place.dto.response;

import com.mycom.backenddaengplace.place.enums.OperationStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class PlaceDetailResponse {
    private Long placeId;
    private String name;
    private String description;
    private String category;
    private String location;
    private Boolean is_parking;
    private Boolean inside;
    private Boolean outside;
    private int weight_limit;
    private int pet_fee;
    private String homepage;
    private OperationStatus operationStatus;
    private OperationHourDto operationHour;
    private String hoilday;
    private Double rating;
    private Boolean is_favorite;
    private long review_count;
    private List<Map<String, Object>> reviews;
}
