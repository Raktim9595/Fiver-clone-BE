package com.raktim.fiverclone.country.controller;

import com.raktim.fiverclone.country.model.CountryEntity;
import com.raktim.fiverclone.country.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/info/country")
@RequiredArgsConstructor
@Tag(name = "Country", description = "Endpoints for countries API")
public class CountryController {
    private final CountryService countryService;

    @GetMapping
    @Operation(
            summary = "Get a list of countries",
            description = "Endpoint to get the list of all the countries available"
    )
    public ResponseEntity<List<CountryEntity>> getAllCountries() {
        return ResponseEntity.ok(countryService.findAll());
    }
}
