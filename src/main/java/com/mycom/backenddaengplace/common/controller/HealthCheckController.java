package com.mycom.backenddaengplace.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> detailedHealthCheck() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("timestamp", LocalDateTime.now());
        healthStatus.put("service", "Daeng Place API");

        return ResponseEntity.ok(healthStatus);
    }

    @GetMapping("/hc")
    public ResponseEntity<String> slashHealthCheck() {
        return ResponseEntity.ok("OK");
    }
}
