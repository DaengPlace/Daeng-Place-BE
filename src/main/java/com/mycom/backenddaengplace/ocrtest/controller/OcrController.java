package com.mycom.backenddaengplace.ocrtest.controller;

import com.mycom.ocrtest.service.OcrService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ocr")
public class OcrController {

    private final OcrService ocrService;

    @Autowired
    public OcrController(OcrService ocrService) {
        this.ocrService = ocrService;
    }

    @GetMapping("/upload")
    public String showUploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = ocrService.saveFile(file);
            return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/analyze")
    public ResponseEntity<List<String>> analyzeImage(@RequestParam String filePath) {
        try {
            String ocrResult = ocrService.performOCR(filePath); // OCR 수행
            List<String> inferTexts = extractInferTexts(ocrResult); // 텍스트만 추출
            return ResponseEntity.ok(inferTexts); // 결과 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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
                    String inferText = (String) field.get("inferText"); // 텍스트만 추출
                    inferTexts.add(inferText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inferTexts;
    }

    @PostMapping("/format")
    public ResponseEntity<String> formatInferText(@RequestBody String jsonData) {
        try {
            String result = ocrService.removeVertices(jsonData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
