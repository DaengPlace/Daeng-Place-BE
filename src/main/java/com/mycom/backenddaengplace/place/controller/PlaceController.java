package com.mycom.backenddaengplace.place.controller;


import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.place.dto.request.SearchCriteria;
import com.mycom.backenddaengplace.place.dto.response.AgeGenderPlaceResponse;
import com.mycom.backenddaengplace.place.dto.response.PlaceDetailResponse;
import com.mycom.backenddaengplace.place.dto.response.PlaceListResponse;
import com.mycom.backenddaengplace.place.dto.response.PopularPlaceResponse;
import com.mycom.backenddaengplace.place.enums.Category;
import com.mycom.backenddaengplace.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@Slf4j
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/{placeId}")
    public ResponseEntity<ApiResponse<PlaceDetailResponse>> getPlaceDetail(
            @PathVariable Long placeId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Member member = customOAuth2User.getMember();
        PlaceDetailResponse response = placeService.getPlaceDetail(placeId, member.getId());
        return ResponseEntity.ok(ApiResponse.success("OK", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PlaceListResponse>> searchPlaces(
            @ModelAttribute SearchCriteria criteria,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Member member = customOAuth2User.getMember();
        PlaceListResponse response = placeService.searchPlaces(criteria, member.getId());
        return ResponseEntity.ok(ApiResponse.success("OK", response));
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<PopularPlaceResponse>>> getPopularPlaces(
            @RequestParam(value = "sort", defaultValue = "popularity") String sort,
            @RequestParam(value = "category", required = false) Category category,
            Pageable pageable) {

        validateSortType(sort);  // 여기서 검증

        Page<PopularPlaceResponse> page = placeService.getPopularPlaces(sort, category, pageable);
        return ResponseEntity.ok(ApiResponse.success("인기 장소 목록 조회를 성공했습니다.", page.getContent()));
    }

    @GetMapping("/gender-popular")
    public ResponseEntity<ApiResponse<AgeGenderPlaceResponse>> getGenderAgePopularPlace(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        Member member = customOAuth2User.getMember();
        AgeGenderPlaceResponse response = placeService.getPopularPlacesByGenderAndAge(member.getId());
        return ResponseEntity.ok(ApiResponse.success("OK", response));
    }

    @GetMapping("/popular/cafe")
    public ResponseEntity<ApiResponse<List<PopularPlaceResponse>>> getPopularCafes(
            @RequestParam(value = "sort", defaultValue = "popularity") String sort) {

        validateSortType(sort);
        List<PopularPlaceResponse> cafes = placeService.getPopularCafes(sort);
        return ResponseEntity.ok(ApiResponse.success("야외 시설이 있는 인기 카페 TOP 5 조회를 성공했습니다.", cafes));
    }

    // 검증 메서드는 Controller 클래스 내부의 private 메서드로 추가
    private void validateSortType(String sort) {
        List<String> validSortTypes = Arrays.asList("popularity", "rating", "review");
        if (!validSortTypes.contains(sort)) {
            throw new InvalidParameterException("정렬 기준이 유효하지 않습니다.");
        }
    }
}
