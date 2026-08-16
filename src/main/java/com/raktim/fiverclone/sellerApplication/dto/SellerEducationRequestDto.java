package com.raktim.fiverclone.sellerApplication.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record SellerEducationRequestDto(
        @NotNull
        String institutionName,

        @NotNull
        String country,

        @NotNull
        String degree,

        @NotNull
        String fieldOfStudy,

        @NotNull
        @Positive
        Integer startYear,

        @Positive
        Integer endYear,

        Boolean current
) {
    public SellerEducationRequestDto {
        current = Boolean.TRUE.equals(current);
    }
}
