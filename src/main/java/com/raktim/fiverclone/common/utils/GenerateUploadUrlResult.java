package com.raktim.fiverclone.common.utils;

import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record GenerateUploadUrlResult(
        String uploadUrl,
        Instant expiresAt
) {
}
