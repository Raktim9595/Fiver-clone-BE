package com.raktim.fiverclone.seller.dto;

import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevelDto;
import com.raktim.fiverclone.seeds.skills.SkillDto;
import com.raktim.fiverclone.user.DTO.UserListResponseDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record SellerResponseDto(
        UUID id,
        String description,
        UserListResponseDto baseUser,
        Integer totalOrders,
        BigDecimal totalEarnings,
        ExperienceLevelDto experience,
        List<SkillDto> skills
) {
}
