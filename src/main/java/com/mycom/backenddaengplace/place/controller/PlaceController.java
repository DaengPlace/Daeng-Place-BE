package com.mycom.backenddaengplace.place.controller;


import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.place.dto.request.SearchCriteria;
import com.mycom.backenddaengplace.place.dto.response.PlaceDetailResponse;
import com.mycom.backenddaengplace.place.dto.response.PlaceListResponse;
import com.mycom.backenddaengplace.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@Slf4j
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/{placeId}")
    public ResponseEntity<ApiResponse<PlaceDetailResponse>> getPlaceDetail(@PathVariable Long placeId) {

        PlaceDetailResponse response = placeService.getPlaceDetail(placeId);
        return ResponseEntity.ok(ApiResponse.success("OK", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PlaceListResponse>> searchPlaces(
            @ModelAttribute SearchCriteria criteria, Pageable pageable) {

        PlaceListResponse response = placeService.searchPlaces(criteria, pageable);
        return ResponseEntity.ok(ApiResponse.success("OK", response));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<PlaceDetailResponse>>> getPopularPlaces() {
        List<PlaceDetailResponse> popularPlaces = placeService.getPopularPlaces();

        return ResponseEntity.ok(ApiResponse.success("인기 장소 목록 조회를 성공했습니다.", popularPlaces));
    }
}
