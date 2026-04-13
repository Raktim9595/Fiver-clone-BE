package com.raktim.fiverclone.common.DTO;


import java.time.LocalDateTime;

public record ApiResponseDTO<T>(
        LocalDateTime timestamp,
        int status,
        String message,
        String path,
        T data
) {

}
