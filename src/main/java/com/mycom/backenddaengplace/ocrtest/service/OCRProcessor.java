package com.mycom.backenddaengplace.ocrtest.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OCRProcessor {
    public List<String> extractInferTexts(String jsonData) {
        List<String> inferTexts = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray images = jsonObject.getJSONArray("images");

            for (int i = 0; i < images.length(); i++) {
                JSONObject image = images.getJSONObject(i);
                JSONArray fields = image.getJSONArray("fields");

                for (int j = 0; j < fields.length(); j++) {
                    JSONObject field = fields.getJSONObject(j);

                    // `vertices`를 무시하고 `inferText`만 추출
                    if (field.has("inferText")) {
                        inferTexts.add(field.getString("inferText"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inferTexts;
    }

    public String preprocessJson(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray images = jsonObject.getJSONArray("images");

            for (int i = 0; i < images.length(); i++) {
                JSONObject image = images.getJSONObject(i);
                JSONArray fields = image.getJSONArray("fields");

                for (int j = 0; j < fields.length(); j++) {
                    JSONObject field = fields.getJSONObject(j);
                    // boundingPoly 필드 제거
                    field.remove("boundingPoly");
                }
            }

            // 전처리된 JSON 데이터를 문자열로 반환
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // 실패 시 빈 JSON 반환
        }
    }
}
