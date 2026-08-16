package com.raktim.fiverclone.sellerApplication.dto;

import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SellerApplicationHistoryRequestDto(
        @NotNull(message = "User Id of the user who changed is required")
        UUID changedBy,

        @NotNull(message = "previousStatus is required")
        SellerApplicationStatus previousStatus,

        @NotNull(message = "newStatus is required")
        SellerApplicationStatus newStatus,

        @NotNull(message = "reason is required")
        String reason
) {
}
