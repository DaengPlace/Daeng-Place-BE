package com.mycom.backenddaengplace.ocrtest.service;

import com.mycom.ocrtest.repository.OcrResultRepository;
import org.apache.tomcat.util.json.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class OcrServiceImpl implements OcrService {

    @Value("${ocr.api.url}")
    private String ocrApiUrl;

    @Value("${ocr.api.secret-key}")
    private String ocrSecretKey;

    private final OcrResultRepository repository;

    public OcrServiceImpl(OcrResultRepository repository) {
        this.repository = repository;
    }

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        String uploadDir = Paths.get(System.getProperty("user.dir"), "uploads").toString();
        File directory = new File(uploadDir);

        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create directory: " + uploadDir);
        }

        File targetFile = new File(uploadDir, file.getOriginalFilename());
        file.transferTo(targetFile);

        return targetFile.getAbsolutePath();
    }

    private void writeMultiPart(OutputStream os, String jsonMessage, File file, String boundary) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition:form-data; name=\"message\"\r\n\r\n");
        sb.append(jsonMessage).append("\r\n");
        os.write(sb.toString().getBytes());

        if (file != null && file.isFile()) {
            sb = new StringBuilder();
            sb.append("--").append(boundary).append("\r\n");
            sb.append("Content-Disposition:form-data; name=\"file\"; filename=\"").append(file.getName()).append("\"\r\n");
            sb.append("Content-Type: application/octet-stream\r\n\r\n");
            os.write(sb.toString().getBytes());

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
            os.write("\r\n".getBytes());
        }
        os.write(("--" + boundary + "--\r\n").getBytes());
    }

    @Override
    public String performOCR(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        URL url = new URL(ocrApiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=OCRBoundary");
        connection.setRequestProperty("X-OCR-SECRET", ocrSecretKey);

        JSONObject json = new JSONObject();
        json.put("version", "V1");
        json.put("requestId", UUID.randomUUID().toString());
        json.put("timestamp", System.currentTimeMillis());

        JSONObject image = new JSONObject();
        image.put("format", "jpg");
        image.put("name", "test_image");

        JSONArray images = new JSONArray();
        images.add(image);
        json.put("images", images);

        try (OutputStream os = connection.getOutputStream()) {
            writeMultiPart(os, json.toString(), file, "OCRBoundary");
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } else {
            throw new IOException("Failed to process OCR: HTTP " + responseCode);
        }
    }

    @Override
    public String processJsonData(String jsonData) {
        return extractConcatenatedText(jsonData);
    }

    @Override
    public String removeVertices(String jsonData) {
        try {
            JSONParser parser = new JSONParser(jsonData);
            JSONObject jsonObject = (JSONObject) parser.parse();

            JSONArray images = (JSONArray) jsonObject.get("images");

            for (Object imageObj : images) {
                JSONObject image = (JSONObject) imageObj;
                JSONArray fields = (JSONArray) image.get("fields");

                for (Object fieldObj : fields) {
                    JSONObject field = (JSONObject) fieldObj;

                    String[] keysToRemove = {"vertices","boundingPoly", "inferConfidence", "valueType"};
                    for (String key : keysToRemove) {
                        field.remove(key);
                    }
                }
            }
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonData;
        }
    }

    private String extractConcatenatedText(String jsonData) {
        try {
            JSONParser parser = new JSONParser(jsonData);
            JSONObject jsonObject = (JSONObject) parser.parse();
            JSONArray images = (JSONArray) jsonObject.get("images");

            StringBuilder concatenatedText = new StringBuilder();

            for (Object imageObj : images) {
                JSONObject image = (JSONObject) imageObj;
                JSONArray fields = (JSONArray) image.get("fields");

                for (Object fieldObj : fields) {
                    JSONObject field = (JSONObject) fieldObj;
                    concatenatedText.append(((String) field.get("inferText")).trim());
                }
            }
            return concatenatedText.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
