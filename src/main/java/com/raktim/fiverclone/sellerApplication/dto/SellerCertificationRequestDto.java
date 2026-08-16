package com.raktim.fiverclone.sellerApplication.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SellerCertificationRequestDto(
        @NotNull
        String certificationName,

        @NotNull
        String issuingOrganization,

        @NotNull
        LocalDate issueDate,

        @NotNull
        LocalDate expirationDate,

        @NotNull
        String credentialId,

        @NotNull
        String credentialUrl
) {
}
