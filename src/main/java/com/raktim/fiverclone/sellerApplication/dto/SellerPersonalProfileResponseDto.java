package com.raktim.fiverclone.sellerApplication.dto;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record SellerPersonalProfileResponseDto(
        UUID id,
        String displayName,
        String professionalHeadline,
        String description,
        String country,
        String phoneNumber,
        Set<String> languages,
        Instant createdAt
) {
}
