package com.raktim.fiverclone.sellerApplication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record SellerPersonalProfileRequestDto(
        @NotNull
        String displayName,

        @NotNull
        String professionalHeadline,

        @NotNull
        String description,

        @NotNull
        String country,

        @NotNull(message = "Phone number is required")
        @Pattern(regexp = "^0[0-9]{9}$", message = "Invalid Australian phone number")
        String phoneNumber,

        @NotEmpty
        Set<UUID> languages // list of UUID which will be equivalent to the language UUID'S
) {
}
