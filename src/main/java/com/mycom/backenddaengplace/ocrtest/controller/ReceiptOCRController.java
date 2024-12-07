package com.mycom.backenddaengplace.ocrtest.controller;

import com.mycom.backenddaengplace.ocrtest.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/receipt")
public class ReceiptOCRController {
    private final OcrService ocrService;

    @Autowired
    public ReceiptOCRController(OcrService ocrService) {
        this.ocrService = ocrService;
    }
    @PostMapping("/process")
    public ResponseEntity<String> processInferText(@RequestBody String jsonData) {
        try {
            String result = ocrService.removeVertices(jsonData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
