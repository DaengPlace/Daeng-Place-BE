package com.mycom.backenddaengplace.pet.controller;

import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.pet.dto.request.BasePetRequest;
import com.mycom.backenddaengplace.pet.dto.request.BreedSearchRequest;
import com.mycom.backenddaengplace.pet.dto.response.*;
import com.mycom.backenddaengplace.pet.service.BreedService;
import com.mycom.backenddaengplace.pet.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member/pets")
@RequiredArgsConstructor
@Slf4j
public class PetController {
    private final PetService petService;
    private final BreedService breedService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<BasePetResponse>> registerPet(
            @Valid @RequestBody BasePetRequest request) {
        log.debug("반려견 등록 요청: {}", request);
        BasePetResponse response = petService.registerPet(request);
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

    @PostMapping("/{petId}")
    public ResponseEntity<ApiResponse<BasePetResponse>> revisePet(
            @Valid @RequestBody BasePetRequest request, @PathVariable Long petId
    ) {
        BasePetResponse response = petService.revisePet(request, petId);
        return ResponseEntity.ok(ApiResponse.success("반려견 수정 성공", response));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<ApiResponse<PetDeleteResponse>> deletePet(
            @PathVariable Long petId
    ) {
        PetDeleteResponse response = petService.deletePet(petId);
        return ResponseEntity.ok(ApiResponse.success("반려견 삭제 성공", response));
    }
}
