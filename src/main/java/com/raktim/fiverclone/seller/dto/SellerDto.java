package com.raktim.fiverclone.seller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder(toBuilder = true)
public record SellerDto(
        @NotNull(message = "User Id is required")
        UUID userId,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Experience is required")
        UUID experienceId,

        @NotNull(message = "Skills is required")
        @NotEmpty(message = "Skills list should not be empty")
        Set<UUID> skills

) {
}
