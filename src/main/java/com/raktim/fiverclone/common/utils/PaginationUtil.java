package com.raktim.fiverclone.common.utils;

import com.raktim.fiverclone.common.DTO.PaginatedResponseDto;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.function.Function;

@NoArgsConstructor
public final class PaginationUtil {
    public static <T, R> PaginatedResponseDto<R> build(Page<T> page, Function<T, R> mapper) {
        return new PaginatedResponseDto<>(
                page.getContent().stream().map(mapper).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getTotalPages(),
                page.getSize()
        );
    }
}