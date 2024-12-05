package com.mycom.backenddaengplace.ocrtest.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OcrService {
    String saveFile(MultipartFile file) throws IOException;

    String performOCR(String filePath) throws IOException;

    String processJsonData(String jsonData);

    String removeVertices(String jsonData);
}
