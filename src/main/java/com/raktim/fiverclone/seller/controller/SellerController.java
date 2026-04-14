package com.raktim.fiverclone.seller.controller;

import com.raktim.fiverclone.seller.dto.SellerDto;
import com.raktim.fiverclone.seller.dto.SellerResponseDto;
import com.raktim.fiverclone.seller.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final SellerService sellerService;

    @PostMapping("/seller")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new seller", description = "Endpoint to add a new seller from a existing user")
    public ResponseEntity<SellerResponseDto> addSeller(@RequestBody SellerDto sellerDto) {
        SellerResponseDto result = sellerService.addSeller(sellerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
