package com.raktim.fiverclone.language.controller;

import com.raktim.fiverclone.language.dto.LanguageDto;
import com.raktim.fiverclone.language.service.LanguageService;
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
@RequestMapping("/api/info/language")
@RequiredArgsConstructor
@Tag(name = "Language", description = "Endpoints for languages")
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Get a list of languages",
            description = "Endpoint to get all the list of languages"

    )
    public ResponseEntity<List<LanguageDto>> getAllLanguages() {
        List<LanguageDto> result = languageService.findAll();
        return ResponseEntity.ok(result);
    }
}
