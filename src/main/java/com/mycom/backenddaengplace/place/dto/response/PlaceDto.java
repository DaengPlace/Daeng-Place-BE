package com.mycom.backenddaengplace.place.dto.response;

import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.place.enums.WeatherType;
import lombok.Data;

import java.util.List;

@Data
public class PlaceDto {
    private Long placeId;
    private String name;
    private Category category;
    private String location;
    private Boolean is_parking;
    private WeatherType weather_type;
    private int weight_limit;
    private int pet_fee;
    private Double latitude;
    private Double longitude;
//    private float rating;
    private List<OperationHourDto> operationHours;
}
