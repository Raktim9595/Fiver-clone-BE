package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.sellerApplication.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller-application")
@RequiredArgsConstructor
@Tag(name = "SellerApplication", description = "List of become a seller API's")
public class SellerApplicationController {
    private final SellerApplicationService sellerApplicationService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new seller", description = "Endpoint to add a new seller in the DRAFT state")
    public ResponseEntity<SellerApplicationEntity> addSeller(
            @Valid @RequestBody StartSellerApplicationRequestDto startSellerApplicationRequestDto
            ) {
        SellerApplicationEntity result = sellerApplicationService
                .startSellerApplication(startSellerApplicationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
