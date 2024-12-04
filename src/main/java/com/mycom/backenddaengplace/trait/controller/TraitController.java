package com.mycom.backenddaengplace.trait.controller;

import com.mycom.backenddaengplace.auth.dto.CustomOAuth2User;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.trait.dto.request.MemberTraitResponseRequestList;
import com.mycom.backenddaengplace.trait.dto.request.PetTraitResponseRequestList;
import com.mycom.backenddaengplace.trait.dto.response.TraitQuestionResponse;
import com.mycom.backenddaengplace.trait.enums.QuestionTarget;
import com.mycom.backenddaengplace.trait.service.MemberTraitService;
import com.mycom.backenddaengplace.trait.service.PetTraitService;
import com.mycom.backenddaengplace.trait.service.TraitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traits")
@RequiredArgsConstructor
public class TraitController {

    private final TraitService traitService;
    private final MemberTraitService memberTraitService;
    private final PetTraitService petTraitService;

    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<List<TraitQuestionResponse>>> getTraitQuestions(@RequestParam QuestionTarget target) {
        return ResponseEntity.ok(ApiResponse.success("성향 질문 목록을 조회했습니다.",
                traitService.getTraitQuestions(target)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerMemberTraitResponse(@RequestBody MemberTraitResponseRequestList request,
                                                                         @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        memberTraitService.saveMemberTraitResponse(request, customOAuth2User.getMember());
        return ResponseEntity.ok(ApiResponse.success("성향 검사 결과를 등록했습니다."));
    }

    @PostMapping("/{petId}")
    public ResponseEntity<ApiResponse<Void>> registerPetTraitResponse(@RequestBody PetTraitResponseRequestList request,
                                                                      @PathVariable Long petId) {
        petTraitService.savePetTraitResponse(request, petId);
        return ResponseEntity.ok(ApiResponse.success("성향 검사 결과를 등록했습니다."));
    }
}
