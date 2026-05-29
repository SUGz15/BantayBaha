package com.bantaybaha.config;

import com.bantaybaha.util.ApiResponse;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    ResponseEntity<ApiResponse<Map<String, String>>> health() {
        Map<String, String> status = Map.of(
                "service", "bantaybaha-backend",
                "status", "up"
        );

        return ResponseEntity.ok(ApiResponse.success("Backend is running", status));
    }
}
