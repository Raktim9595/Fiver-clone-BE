package com.raktim.fiverclone.sellerApplication.dto;

import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record SellerApplicationHistoryResponseDto(
        UUID id,
        UUID applicationId,
        String changedBy,
        String reason,
        SellerApplicationStatus currentStatus,
        Instant createdAt
) {
}
