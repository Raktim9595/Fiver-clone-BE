package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.sellerApplication.dto.SellerProfessionalProfileRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerProfessionalProfileResponseDto;
import com.raktim.fiverclone.sellerApplication.service.SellerProfessionalProfileService;
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
@RequiredArgsConstructor
@RequestMapping("/api/seller-application")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "SellerApplication", description = "List of become a seller API's")
public class SellerProfessionalProfileController {
    public final SellerProfessionalProfileService service;

    @PostMapping("/{id}/professional-profile")
    @Operation(
            summary = "End point to create the professional profile of the respective seller application"
    )
    public ResponseEntity<SellerProfessionalProfileResponseDto>
        createSellerProfessionalProfile(
            @PathVariable UUID id,
            @Valid @RequestBody SellerProfessionalProfileRequestDto dto
    ) {
        SellerProfessionalProfileResponseDto result = service.createProfessionalProfile(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
