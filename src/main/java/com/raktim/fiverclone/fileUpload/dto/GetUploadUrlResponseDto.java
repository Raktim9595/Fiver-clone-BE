package com.raktim.fiverclone.fileUpload.dto;

import com.raktim.fiverclone.fileUpload.utils.FileStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record GetUploadUrlResponseDto(
        UUID id,
        String uploadUrl,
        String s3Key,
        FileStatus status,
        Instant expiresAt
) {
}
