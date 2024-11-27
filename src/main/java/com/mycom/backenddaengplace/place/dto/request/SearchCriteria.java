package com.mycom.backenddaengplace.place.dto.request;

import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.place.enums.WeatherType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCriteria {
    private String search;       // 검색어
    private String address;      // 주소 검색
    private Category category;   // 카테고리
    private Boolean isParking;   // 주차 가능 여부
    private WeatherType weatherType;  // 실내 or 야외 공간
    private Integer weightLimit; // 무게 제한
    private Integer petFee;      // 애견 동반 요금
    private Double latitude;     // 위도
    private Double longitude;    // 경도
    private String sortBy;       // 정렬 기준
}