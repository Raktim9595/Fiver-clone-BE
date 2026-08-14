package com.raktim.fiverclone.seller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StartSellerApplicationRequestDto(
        @NotNull(message = "User id is required")
        UUID userId
) {
}
