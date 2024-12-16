package com.mycom.backenddaengplace.place.dto.request;

import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.place.enums.OperationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCriteria {
    private String search;       // 검색어
    private String address;      // 주소 검색
    private Category category;   // 카테고리
    private Boolean isParking;   // 주차 가능 여부
    private Boolean inside;  // 실내 공간
    private Boolean outside;       // 야외 공간
    private Integer weightLimit; // 무게 제한
    private Integer petFee; // 동반 요금
    private Boolean isOpen; // 영업 중
    private OperationStatus operationStatus; // 운영 상태
    private Double latitude;     // 위도
    private Double longitude;    // 경도
    private String sortBy;       // 정렬 기준
}