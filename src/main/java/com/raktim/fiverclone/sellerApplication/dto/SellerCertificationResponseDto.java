package com.raktim.fiverclone.sellerApplication.dto;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record SellerCertificationResponseDto(
        UUID id,
        UUID applicationId,
        Instant createdAt,
        String certificationName,
        String issuingOrganization,
        LocalDate issueDate,
        LocalDate expirationDate,
        String credentialId,
        String credentialUrl
) {}
