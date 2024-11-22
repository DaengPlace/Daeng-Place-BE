package com.mycom.backenddaengplace.place.controller;


import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.place.dto.response.PlaceDetailResponse;
import com.mycom.backenddaengplace.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/{placeId}")
    public ResponseEntity<ApiResponse<PlaceDetailResponse>> getPlaceDetail(@PathVariable Long placeId) {

        PlaceDetailResponse response = placeService.getPlaceDetail(placeId);
        return ResponseEntity.ok(ApiResponse.success("OK", response));
    }
}
