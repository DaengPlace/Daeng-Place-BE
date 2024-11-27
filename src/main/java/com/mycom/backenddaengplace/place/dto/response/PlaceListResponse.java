package com.mycom.backenddaengplace.place.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlaceListResponse {
    List<PlaceDto> places;
    boolean isLast;
}
