package com.raktim.fiverclone.common.DTO;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String errorCode,
        String message,
        String path,
        Map<String, String> validationErrors
) {

    public ErrorResponseDTO(
            LocalDateTime timestamp,
            int status,
            String errorCode,
            String message,
            String path
    ) {
        this(timestamp, status, errorCode, message, path, null);
    }
}