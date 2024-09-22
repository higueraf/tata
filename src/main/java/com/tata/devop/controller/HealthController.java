package com.tata.devop.controller;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final Dotenv dotenv = Dotenv.load();

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Tata-Test");
        response.put("status", "UP");
        response.put("environment", dotenv.get("APP_ENVIRONMENT", "dev"));
        response.put("timestamp", LocalDateTime.now());
        return response;
    }
}
