package com.mycom.backenddaengplace.ocrtest.controller;

import com.mycom.backenddaengplace.ocrtest.service.OcrService;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipt")
public class ReceiptOCRController {
    private final OcrService ocrService;

    @Autowired
    public ReceiptOCRController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<String>> processInferText(@RequestBody String jsonData) {
        if (jsonData == null || jsonData.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid input: JSON data is empty or null", "400"));
        }

        try {
            String result = ocrService.removeVertices(jsonData);
            return ResponseEntity.ok(ApiResponse.success("Receipt processing completed", result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error processing receipt: " + e.getMessage(), "500"));
        }
    }
}
