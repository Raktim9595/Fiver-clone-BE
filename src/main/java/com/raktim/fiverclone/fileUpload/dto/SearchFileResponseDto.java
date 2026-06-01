package com.raktim.fiverclone.fileUpload.dto;

import com.raktim.fiverclone.fileUpload.utils.FileType;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record SearchFileResponseDto(
        UUID id,
        String imageUrl,
        FileType type
) {
}
