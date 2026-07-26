package com.raktim.fiverclone.seeds.experienceLevel;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/experienceLevel")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Experience Level", description = "Endpoints for everything related to experience level")
public class ExperienceLevelController {
    private final ExperienceLevelService service;

    @GetMapping
    @Operation(
            summary = "Get the list of experience levels",
            description = "End point to fetch all of the experience levels present in the db"
    )
    public ResponseEntity<List<ExperienceLevelEntity>> getExperienceLevels() {
        return ResponseEntity.ok(service.getExperienceLevels());
    }
}
