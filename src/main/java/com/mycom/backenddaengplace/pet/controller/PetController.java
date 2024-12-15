package com.mycom.backenddaengplace.pet.controller;

import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.member.domain.Member;
import com.mycom.backenddaengplace.pet.dto.request.BasePetRequest;
import com.mycom.backenddaengplace.pet.dto.request.BreedSearchRequest;
import com.mycom.backenddaengplace.pet.dto.response.BasePetResponse;
import com.mycom.backenddaengplace.pet.dto.response.BreedSearchResponse;
import com.mycom.backenddaengplace.pet.dto.response.PetDeleteResponse;
import com.mycom.backenddaengplace.pet.service.BreedService;
import com.mycom.backenddaengplace.pet.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/member/pets")
@RequiredArgsConstructor
@Slf4j
public class PetController {
    private final PetService petService;
    private final BreedService breedService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<BasePetResponse>> registerPet(
            @Valid @RequestBody BasePetRequest request,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        BasePetResponse response = petService.registerPet(request, member.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("반려견이 등록되었습니다.", response));
    }

    @GetMapping("/breed-types")
    public ResponseEntity<ApiResponse<BreedSearchResponse>> searchBreedTypes(
            @RequestParam(required = false) String keyword) {
        log.debug("견종 검색 요청. keyword: {}", keyword);

        BreedSearchResponse response = breedService.searchBreedTypes(
                BreedSearchRequest.of(keyword)
        );

        return ResponseEntity.ok(ApiResponse.success(
                String.format("견종 검색 완료 (총 %d건)", response.getTotalCount()),
                response
        ));
    }

    @GetMapping("/{petId}")
    public ResponseEntity<ApiResponse<BasePetResponse>> getPet(@PathVariable Long petId) {
        BasePetResponse response = petService.getPet(petId);
        return ResponseEntity.ok(ApiResponse.success("반려견 조회 성공", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BasePetResponse>>> getAllPets(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Long memberId = customOAuth2User.getMember().getId();
        return ResponseEntity.ok(ApiResponse.success("반려견 목록 조회 성공", petService.getPets(memberId)));
    }

    @PutMapping("/{petId}")
    public ResponseEntity<ApiResponse<BasePetResponse>> revisePet(
            @Valid @RequestBody BasePetRequest request,
            @PathVariable Long petId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User  // 인증 정보 추가
    ) {
        Member member = customOAuth2User.getMember();
        BasePetResponse response = petService.revisePet(request, petId, member.getId());
        return ResponseEntity.ok(ApiResponse.success("반려견 수정 성공", response));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<ApiResponse<PetDeleteResponse>> deletePet(
            @PathVariable Long petId,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User  // 인증 정보 추가
    ) {
        Member member = customOAuth2User.getMember();
        PetDeleteResponse response = petService.deletePet(petId, member.getId());
        return ResponseEntity.ok(ApiResponse.success("반려견 삭제 성공", response));
    }

    @PostMapping("/{petId}/profile/image")  // URL 패턴도 member와 비슷하게
    public ResponseEntity<ApiResponse<BasePetResponse>> updateProfileImage(
            @PathVariable Long petId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User
    ) {
        Member member = customOAuth2User.getMember();
        BasePetResponse response = petService.updateProfileImage(petId, file, member.getId());
        return ResponseEntity.ok(ApiResponse.success("반려견 프로필 이미지가 업데이트되었습니다.", response));
    }
}
