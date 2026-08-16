package com.raktim.fiverclone.sellerApplication.dto;

import com.raktim.fiverclone.sellerApplication.enums.PortfolioLinkType;
import jakarta.validation.constraints.NotNull;

public record SellerPortfolioRequestDto(
        @NotNull
        PortfolioLinkType linkType,

        @NotNull
        String title,

        @NotNull
        String url
) {
}
