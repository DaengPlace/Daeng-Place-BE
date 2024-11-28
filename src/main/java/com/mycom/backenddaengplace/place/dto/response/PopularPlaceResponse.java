package com.mycom.backenddaengplace.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PopularPlaceResponse {
    private String type;
    private Long placeId;
    private String name;
    private Double rating;
}
