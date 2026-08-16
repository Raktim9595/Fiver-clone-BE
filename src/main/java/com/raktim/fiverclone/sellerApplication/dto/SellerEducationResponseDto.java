package com.raktim.fiverclone.sellerApplication.dto;

import java.time.Instant;
import java.util.UUID;

public record SellerEducationResponseDto(
        UUID id,
        String institutionName,
        String country,
        String degree,
        String fieldOfStudy,
        String startYear,
        String endYear,
        Boolean current,
        UUID applicationId,
        Instant createdAt
) {
}
