package com.blog_java.application.controllers.v1;

import com.blog_java.application.services.StatusService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/status")
@SecurityRequirement(name = "bearer-key")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public Map<String, Object> getStatus() {
        return Map.of(
                "updated_at", Instant.now().toString(),
                "dependencies", Map.of(
                        "database", statusService.getDatabaseStatus()
                )
        );
    }
}

