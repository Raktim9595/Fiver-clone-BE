package com.raktim.fiverclone.common.DTO;

import java.util.List;

public record PaginatedResponseDto<T>(
        List<T> items,
        long total,
        int page,
        int totalPages,
        int pageSize
) {

    public PaginatedResponseDto() {
        this(List.of(), 0, 0, 0, 0);
    }
}