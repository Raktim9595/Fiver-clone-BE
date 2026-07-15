package com.raktim.fiverclone.timezone.controller;

import com.raktim.fiverclone.timezone.model.TimeZoneEntity;
import com.raktim.fiverclone.timezone.service.TimezoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/info/timezone")
@Tag(name = "Timezone", description = "Endpoints for timezone API")
public class TimezoneController {
    private final TimezoneService timezoneService;

    @GetMapping
    @Operation(
            summary = "Get list of timezones",
            description = "End point to fetch the list of all the timezone entities present in the DB"
    )
    public ResponseEntity<List<TimeZoneEntity>> getAllTimezones() {
        return ResponseEntity.ok(timezoneService.findAll());
    }
}
