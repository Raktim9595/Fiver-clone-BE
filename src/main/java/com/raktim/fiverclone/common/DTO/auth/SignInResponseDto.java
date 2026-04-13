package com.raktim.fiverclone.common.DTO.auth;

import jakarta.validation.constraints.NotNull;

public record SignInResponseDto(
        @NotNull
        String token
) {
}
