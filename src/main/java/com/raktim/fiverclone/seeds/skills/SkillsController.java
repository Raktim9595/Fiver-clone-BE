package com.raktim.fiverclone.seeds.skills;

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
@RequestMapping("/api/skills")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Skills", description = "Endpoints for all the ")
public class SkillsController {
    private final SkillsService skillsService;

    @GetMapping
    @Operation(
            summary = "Fetch skills",
            description = "End point which returns list of all the skills available in the db"
    )
    public ResponseEntity<List<SkillEntity>> getSkills() {
        return ResponseEntity.ok(skillsService.findAll());
    }
}
