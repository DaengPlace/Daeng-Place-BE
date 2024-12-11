package com.mycom.backenddaengplace.favorite.controller;


import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.favorite.dto.request.FavoriteRequest;
import com.mycom.backenddaengplace.favorite.dto.response.FavoriteDeleteResponse;
import com.mycom.backenddaengplace.favorite.dto.response.FavoriteRegisterResponse;
import com.mycom.backenddaengplace.favorite.dto.response.FavoritesResponse;
import com.mycom.backenddaengplace.favorite.service.FavoriteService;
import com.mycom.backenddaengplace.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<ApiResponse<FavoritesResponse>> getFavoritesByMember(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        FavoritesResponse response = favoriteService.getFavoritesByMember(member.getId());
        return ResponseEntity.ok(ApiResponse.success("나의 즐겨찾기 목록을 조회하였습니다.", response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<FavoriteRegisterResponse>> registerFavorite(
            @Valid @RequestBody FavoriteRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        log.debug("즐겨찾기 등록 요청: {}", request);
        FavoriteRegisterResponse response = favoriteService.registerFavorite(request, member.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("즐겨찾기가 등록되었습니다.", response));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<FavoriteDeleteResponse>> deleteFavorite(
            @Valid @RequestBody FavoriteRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        log.debug("즐겨찾기 삭제 요청: {}", request);
        favoriteService.deleteFavorite(request, member);
        return ResponseEntity.ok(ApiResponse.success("즐겨찾기가 삭제되었습니다."));
    }

}
