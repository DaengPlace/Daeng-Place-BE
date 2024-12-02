package com.mycom.backenddaengplace.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/test")
public class ApiTestController {

    @GetMapping
    public HashMap<String, String> testApi() {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "success");

        return response;
    }

}