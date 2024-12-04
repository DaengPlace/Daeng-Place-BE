package com.mycom.backenddaengplace.trait.controller;

import com.mycom.backenddaengplace.common.dto.ApiResponse;
import com.mycom.backenddaengplace.trait.dto.response.TraitQuestionResponse;
import com.mycom.backenddaengplace.trait.enums.QuestionTarget;
import com.mycom.backenddaengplace.trait.service.TraitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/traits")
@RequiredArgsConstructor
public class TraitController {

    private final TraitService traitService;

    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<List<TraitQuestionResponse>>> getTraitQuestions(@RequestParam QuestionTarget target) {
        return ResponseEntity.ok(ApiResponse.success("성향 질문 목록을 조회했습니다.",
                traitService.getTraitQuestions(target)));
    }
}
