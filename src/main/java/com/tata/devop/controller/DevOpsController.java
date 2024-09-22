package com.tata.devop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/DevOps")
public class DevOpsController {

    @PostMapping
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, String> payload) {
        String to = payload.get("to");
        String responseMessage = "Hello " + to + " your message will be send";
        return ResponseEntity.ok(Map.of("message", responseMessage));
    }
}
