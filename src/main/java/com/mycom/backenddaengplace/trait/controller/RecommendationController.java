package com.mycom.backenddaengplace.trait.controller;

import com.mycom.backenddaengplace.common.dto.ApiResponse;

import com.mycom.backenddaengplace.place.dto.response.PopularPlaceResponse;
import com.mycom.backenddaengplace.trait.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{petId}")
    public ResponseEntity<ApiResponse<List<PopularPlaceResponse>>> getRecommendedPlaces(@PathVariable Long petId) {
        List<PopularPlaceResponse> recommendations = recommendationService.recommendPlaces(petId);
        return ResponseEntity.ok(ApiResponse.success("장소 추천 목록을 조회했습니다.", recommendations));
    }
}
