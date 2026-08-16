package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.sellerApplication.dto.*;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.service.SellerCertificationService;
import com.raktim.fiverclone.sellerApplication.service.SellerEducationService;
import com.raktim.fiverclone.sellerApplication.service.SellerPortfolioService;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "SellerApplication", description = "List of become a seller API's")
public class SellerApplicationController {
    private final SellerApplicationService sellerApplicationService;
    private final SellerEducationService sellerEducationService;
    private final SellerPortfolioService sellerPortfolioService;
    private final SellerCertificationService sellerCertificationService;

    @PostMapping
    @Operation(summary = "Create a new seller", description = "Endpoint to add a new seller in the DRAFT state")
    public ResponseEntity<SellerApplicationEntity> addSeller(
            @Valid @RequestBody StartSellerApplicationRequestDto startSellerApplicationRequestDto
            ) {
        SellerApplicationEntity result = sellerApplicationService
                .startSellerApplication(startSellerApplicationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/{id}/seller-education")
    @Operation(
            summary = "Add a seller education details"
    )
    public ResponseEntity<SellerEducationResponseDto> addSellerEducation(
            @PathVariable UUID id,
            @Valid @RequestBody SellerEducationRequestDto dto
    ) {
        SellerEducationResponseDto result = sellerEducationService.create(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/{id}/seller-portfolio")
    @Operation(
            summary = "Add seller portfolio details in the application"
    )
    public ResponseEntity<SellerPortfolioResponseDto> addSellerPortfolio(
            @PathVariable UUID id,
            @Valid @RequestBody SellerPortfolioRequestDto dto
    ) {
        SellerPortfolioResponseDto result = sellerPortfolioService.create(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/{id}/seller-certification")
    @Operation(
            summary = "Add seller certification in the seller application"
    )
    public ResponseEntity<SellerCertificationResponseDto> addSellerCertification(
            @PathVariable UUID id,
            @Valid @RequestBody SellerCertificationRequestDto dto
    ) {
        SellerCertificationResponseDto result =
                sellerCertificationService.create(id, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
