package com.raktim.fiverclone.sellerApplication.controller;

import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerApplicationHistoryResponseDto;
import com.raktim.fiverclone.sellerApplication.service.sellerApplicationStatusHistory.SellerApplicationStatusHistoryService;
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
public class SellerApplicationHistoryController {
    private final SellerApplicationStatusHistoryService service;
    @PostMapping("/{id}/status-history")
    @Operation(
           summary = "Create a new row for to record status change of the Seller Application"
    )
    public ResponseEntity<SellerApplicationHistoryResponseDto>
        createHistory (
            @PathVariable UUID id,
            @Valid @RequestBody SellerApplicationHistoryRequestDto dto
    ) {
        SellerApplicationHistoryResponseDto result = service.createApplicationHistory(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
