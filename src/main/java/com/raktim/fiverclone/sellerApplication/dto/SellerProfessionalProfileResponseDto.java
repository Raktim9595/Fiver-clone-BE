package com.raktim.fiverclone.sellerApplication.dto;

import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevel;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SellerProfessionalProfileResponseDto(
        UUID id,
        UUID applicationId,
        String occupation,
        Integer yearsOfExperience,
        ExperienceLevel professionalLevel,
        Boolean active
) {
}
