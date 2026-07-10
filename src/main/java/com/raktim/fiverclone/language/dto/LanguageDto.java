package com.raktim.fiverclone.language.dto;

import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record LanguageDto(
        UUID id,
        String code,
        String language
) {
}
