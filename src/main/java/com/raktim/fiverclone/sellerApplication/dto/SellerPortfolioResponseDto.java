package com.raktim.fiverclone.sellerApplication.dto;

import com.raktim.fiverclone.sellerApplication.enums.PortfolioLinkType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record SellerPortfolioResponseDto(
        UUID applicationId,
        UUID id,
        PortfolioLinkType linkType,
        String title,
        String url
) {
}
