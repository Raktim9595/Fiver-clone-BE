package com.raktim.fiverclone.fileUpload.dto;

import com.raktim.fiverclone.fileUpload.utils.FileStatus;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record CompleteFileUploadResponseDto(
        UUID id,
        FileStatus status
) {
}
