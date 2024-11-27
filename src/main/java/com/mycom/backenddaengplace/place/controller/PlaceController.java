package com.mycom.backenddaengplace.place.controller;


import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.place.dto.request.FavoriteDeleteRequest;
import com.mycom.backenddaengplace.place.dto.request.FavoriteRegisterRequest;
import com.mycom.backenddaengplace.place.dto.response.FavoriteDeleteResponse;
import com.mycom.backenddaengplace.place.dto.response.PlaceDetailResponse;
import com.mycom.backenddaengplace.place.dto.response.FavoriteRegisterResponse;
import com.mycom.backenddaengplace.place.dto.request.SearchCriteria;
import com.mycom.backenddaengplace.place.dto.response.PlaceDetailResponse;
import com.mycom.backenddaengplace.place.dto.response.PlaceListResponse;
import com.mycom.backenddaengplace.place.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
