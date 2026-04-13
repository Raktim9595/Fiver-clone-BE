package com.raktim.fiverclone.common.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SearchRequestDto(

        @NotNull(message = "Page number is required")
        @Min(value = 0, message = "Page number must be greater than or equal to 0")
        Integer pageNumber,

        @NotNull(message = "Page size is required")
        @Min(value = 1, message = "Page size must be at least 1")
        Integer pageSize

) {}