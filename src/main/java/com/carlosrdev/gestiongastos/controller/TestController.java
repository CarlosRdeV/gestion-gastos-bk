package com.carlosrdev.gestiongastos.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final Environment environment;

    @Value("${server.port}")
    private String serverPort;

    public TestController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> hello() {
        String[] activeProfiles = environment.getActiveProfiles();
        String profileInfo = activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default";
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hello from Gestion Gastos Backend API");
        response.put("status", "Running successfully");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("profile", profileInfo);
        response.put("port", serverPort);
        response.put("version", "1.0.0");
        
        return ResponseEntity.ok(response);
    }
}