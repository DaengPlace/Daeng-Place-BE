package com.mycom.backenddaengplace.place.dto.response;

import com.mycom.backenddaengplace.member.enums.Gender;
import lombok.Data;

import java.util.List;

@Data
public class AgeGenderPlaceResponse {
    private String age;
    private String gender;
    private List<PopularPlaceResponse> popularPlaces;
}
