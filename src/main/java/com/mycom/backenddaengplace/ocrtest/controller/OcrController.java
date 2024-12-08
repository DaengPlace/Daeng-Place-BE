package com.mycom.backenddaengplace.ocrtest.controller;

import com.mycom.backenddaengplace.ocrtest.service.OcrService;
import com.mycom.backenddaengplace.common.dto.ApiResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ocr")
public class OcrController {

    private final OcrService ocrService;

    @Autowired
    public OcrController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    // 파일 업로드 처리
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = ocrService.saveFile(file);
            return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", filePath));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error uploading file: " + e.getMessage(), "500"));
        }
    }

    // JSON 응답을 위한 API
    @PostMapping("/analyze")
    public ResponseEntity<ApiResponse<String>> analyzeImage(@RequestParam String filePath) {
        try {
            String ocrResult = ocrService.performOCR(filePath);
            return ResponseEntity.ok(ApiResponse.success("OCR analysis completed", ocrResult));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error analyzing image: " + e.getMessage(), "500"));
        }
    }

    // JSON 데이터를 처리하는 API
    @PostMapping("/format")
    public ResponseEntity<ApiResponse<String>> formatInferText(@RequestBody String jsonData) {
        try {
            String result = ocrService.removeVertices(jsonData);
            return ResponseEntity.ok(ApiResponse.success("Text formatting completed", result));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Error formatting text: " + e.getMessage(), "500"));
        }
    }

    // OCR 결과에서 inferTexts 추출
    private List<String> extractInferTexts(String jsonData) {
        List<String> inferTexts = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
            JSONArray images = (JSONArray) jsonObject.get("images");

            for (Object imageObj : images) {
                JSONObject image = (JSONObject) imageObj;
                JSONArray fields = (JSONArray) image.get("fields");

                for (Object fieldObj : fields) {
                    JSONObject field = (JSONObject) fieldObj;
                    String inferText = (String) field.get("inferText");
                    inferTexts.add(inferText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inferTexts;
    }
}
