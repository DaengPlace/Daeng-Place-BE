package com.mycom.backenddaengplace.ocrtest.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class OcrResultProcessor {

    public static String extractInferText(String jsonData) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
            JSONArray images = (JSONArray) jsonObject.get("images");
            JSONArray inferTextArray = new JSONArray();

            for (Object imageObj : images) {
                JSONObject image = (JSONObject) imageObj;
                JSONArray fields = (JSONArray) image.get("fields");

                for (Object fieldObj : fields) {
                    JSONObject field = (JSONObject) fieldObj;
                    String inferText = (String) field.get("inferText");
                    inferTextArray.add(inferText);
                }
            }

            // 결과를 JSON 배열로 반환
            return inferTextArray.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            return "[]"; // 오류 발생 시 빈 배열 반환
        }
    }

    public static void main(String[] args) {
        String jsonData = "YOUR_JSON_DATA_HERE"; // JSON 데이터 문자열 입력
        String result = extractInferText(jsonData);
        System.out.println(result);
    }
}
