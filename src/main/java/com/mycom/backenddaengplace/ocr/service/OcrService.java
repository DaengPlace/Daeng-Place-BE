package com.mycom.backenddaengplace.ocr.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OcrService {
    String saveFile(MultipartFile file) throws IOException;

    String performOCR(String filePath) throws IOException;

    String removeVertices(String jsonData);
}
