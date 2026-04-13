package com.raktim.fiverclone.common.DTO;

import java.time.Instant;
import java.util.UUID;

public record BaseResponseDTO(
        UUID id,
        Instant createdAt,
        Instant updatedAt
) {}