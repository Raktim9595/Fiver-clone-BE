package com.raktim.fiverclone.fileUpload.dto;

import com.raktim.fiverclone.fileUpload.utils.FileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record FileUploadDto(
        @NotBlank(message = "FileName is required")
        String fileName,

        @NotBlank(message = "ContentType is required")
        String contentType,

        @NotNull(message = "FileSize is required")
        @Positive(message = "FileSize must be greater than 0")
        Long fileSize,

        @NotNull(message = "FileType is required")
        FileType type,

        @NotNull(message = "User details are required")
        UUID userId
) {
}
