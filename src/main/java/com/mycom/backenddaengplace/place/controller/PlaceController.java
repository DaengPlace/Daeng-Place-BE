package com.mycom.backenddaengplace.place.controller;


import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.place.dto.request.FavoriteDeleteRequest;
import com.mycom.backenddaengplace.place.dto.request.FavoriteRegisterRequest;
import com.mycom.backenddaengplace.place.dto.response.FavoriteDeleteResponse;
import com.mycom.backenddaengplace.place.dto.response.PlaceDetailResponse;
import com.mycom.backenddaengplace.place.dto.response.FavoriteRegisterResponse;
import com.mycom.backenddaengplace.place.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/favorite")
    public ResponseEntity<ApiResponse<FavoriteRegisterResponse>> registerFavorite(
            @Valid @RequestBody FavoriteRegisterRequest request
    ) {
        log.debug("즐겨찾기 등록 요청: {}", request);
        FavoriteRegisterResponse response = placeService.registerFavorite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("즐겨찾기가 등록되었습니다.", response));
    }

    @DeleteMapping("/favorite")
    public ResponseEntity<ApiResponse<FavoriteDeleteResponse>> deleteFavorite(
            @Valid @RequestBody FavoriteDeleteRequest request
    ) {
        log.debug("즐겨찾기 삭제 요청: {}", request);
        FavoriteDeleteResponse response = placeService.deleteFavorite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("즐겨찾기가 삭제되었습니다.", response));
    }

}
