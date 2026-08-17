package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.sellerApplication.model.OccupationEntity;
import com.raktim.fiverclone.sellerApplication.service.OccupationService;
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
@RequestMapping("/api/occupations")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Occupations", description = "List of end-points regarding occupations")
public class OccupationController {
    private final OccupationService occupationService;

    @GetMapping
    @Operation(
            summary = "End points to fetch all of the occupations present in the db"
    )
    public ResponseEntity<List<OccupationEntity>> findAll() {
        List<OccupationEntity> result = occupationService.findAllOccupations();
        return ResponseEntity.ok().body(result);
    }
}
