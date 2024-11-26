package com.mycom.backenddaengplace.favorite.controller;


import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.favorite.service.FavoriteService;
import com.mycom.backenddaengplace.place.dto.request.FavoriteDeleteRequest;
import com.mycom.backenddaengplace.place.dto.request.FavoriteRegisterRequest;
import com.mycom.backenddaengplace.place.dto.response.FavoriteDeleteResponse;
import com.mycom.backenddaengplace.place.dto.response.FavoriteRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<FavoriteRegisterResponse>> getFavoritesByMember(
            @Valid @RequestBody FavoriteRegisterRequest request
    ) {
        log.debug("즐겨찾기 리스트 요청: {}", request);
        FavoriteRegisterResponse response = favoriteService.registerFavorite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("즐겨찾기가 등록되었습니다.", response));
    }


    @PutMapping("")

    public ResponseEntity<ApiResponse<FavoriteRegisterResponse>> registerFavorite(
            @Valid @RequestBody FavoriteRegisterRequest request
    ) {
        log.debug("즐겨찾기 등록 요청: {}", request);
        FavoriteRegisterResponse response = favoriteService.registerFavorite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("즐겨찾기가 등록되었습니다.", response));
    }

    @DeleteMapping("")
    public ResponseEntity<ApiResponse<FavoriteDeleteResponse>> deleteFavorite(
            @Valid @RequestBody FavoriteDeleteRequest request
    ) {
        log.debug("즐겨찾기 삭제 요청: {}", request);
        favoriteService.deleteFavorite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("즐겨찾기가 삭제되었습니다."));
    }
}
