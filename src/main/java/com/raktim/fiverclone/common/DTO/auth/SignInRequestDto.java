package com.raktim.fiverclone.common.DTO.auth;

import jakarta.validation.constraints.NotNull;

public record SignInRequestDto(
        @NotNull
        String username,
        @NotNull
        String password
) {
}
