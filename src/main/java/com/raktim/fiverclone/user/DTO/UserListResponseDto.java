package com.raktim.fiverclone.user.DTO;

import java.util.UUID;

public record UserListResponseDto(
        UUID id,
        String username,
        String email
) {}