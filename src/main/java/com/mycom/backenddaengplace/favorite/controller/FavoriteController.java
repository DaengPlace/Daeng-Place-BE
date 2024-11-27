package com.mycom.backenddaengplace.favorite.controller;


import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.favorite.dto.request.FavoriteDeleteRequest;
import com.mycom.backenddaengplace.favorite.dto.request.FavoriteRegisterRequest;
import com.mycom.backenddaengplace.favorite.dto.request.FavoritesRequest;
import com.mycom.backenddaengplace.favorite.dto.response.FavoriteDeleteResponse;
import com.mycom.backenddaengplace.favorite.dto.response.FavoriteRegisterResponse;
import com.mycom.backenddaengplace.favorite.dto.response.FavoritesResponse;
import com.mycom.backenddaengplace.favorite.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<List<FavoritesResponse>>> getFavoritesByMember(
            @PathVariable("memberId") Long memberId
    ) {
        log.debug("즐겨찾기 리스트 요청: {}", memberId);
        List<FavoritesResponse> response = favoriteService.getFavoritesByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success("즐겨찾기 리스트를 조회하였습니다.", response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<FavoriteRegisterResponse>> registerFavorite(
            @Valid @RequestBody FavoriteRegisterRequest request
    ) {
        log.debug("즐겨찾기 등록 요청: {}", request);
        FavoriteRegisterResponse response = favoriteService.registerFavorite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("즐겨찾기가 등록되었습니다.", response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<FavoriteDeleteResponse>> deleteFavorite(
            @Valid @RequestBody FavoriteDeleteRequest request
    ) {
        log.debug("즐겨찾기 삭제 요청: {}", request);
        favoriteService.deleteFavorite(request);
        return ResponseEntity.ok(ApiResponse.success("즐겨찾기가 삭제되었습니다."));
    }

}
