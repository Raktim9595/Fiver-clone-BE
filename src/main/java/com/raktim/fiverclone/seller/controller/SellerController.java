package com.raktim.fiverclone.seller.controller;

import com.raktim.fiverclone.seller.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.seller.model.SellerApplicationEntity;
import com.raktim.fiverclone.seller.service.sellerApplication.SellerApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Seller", description = "Sellers API")
public class SellerController {
    private final SellerApplicationService sellerApplicationService;

    @PostMapping("/seller")
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
