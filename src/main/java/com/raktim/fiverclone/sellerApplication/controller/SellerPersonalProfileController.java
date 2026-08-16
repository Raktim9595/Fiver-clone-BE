package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileResponseDto;
import com.raktim.fiverclone.sellerApplication.service.sellerPersonalProfile.SellerPersonalProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/seller-application")
@RequiredArgsConstructor
@Tag(name = "SellerApplication", description = "List of become a seller API's")
@SecurityRequirement(name = "bearerAuth")
public class SellerPersonalProfileController {
    private final SellerPersonalProfileService service;

    @PostMapping("/{id}/personal-profile")
    @Operation(
            summary = "End point to create the personal profile of the respective seller application"
    )
    public ResponseEntity<SellerPersonalProfileResponseDto> createSellerPersonalProfile(
            @PathVariable UUID id,
            @Valid @RequestBody SellerPersonalProfileRequestDto dto
            ) {
        SellerPersonalProfileResponseDto result = service.create(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
