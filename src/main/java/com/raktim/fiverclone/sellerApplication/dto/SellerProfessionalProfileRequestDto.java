package com.raktim.fiverclone.sellerApplication.dto;

import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SellerProfessionalProfileRequestDto(
        @NotNull
        UUID occupationId,

        @NotNull
        Integer yearsOfExperience,

        @NotNull
        ExperienceLevel professionalLevel,

        Boolean active
) {
    public SellerProfessionalProfileRequestDto {
        active = Boolean.TRUE.equals(active);
    }
}
