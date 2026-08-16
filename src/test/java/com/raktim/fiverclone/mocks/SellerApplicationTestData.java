package com.raktim.fiverclone.mocks;

import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileRequestDto;

import java.util.Set;
import java.util.UUID;

public class SellerApplicationTestData {
    public static SellerPersonalProfileRequestDto.SellerPersonalProfileRequestDtoBuilder
        validSellerPersonalProfileRequestDto() {
        Set<UUID> languages = Set.of(UUID.randomUUID(), UUID.randomUUID());
        return SellerPersonalProfileRequestDto.builder()
                .displayName( "alanwalker")
                .country("Nepal")
                .phoneNumber("0406055522")
                .professionalHeadline("I am very excited")
                .description("I am motivated software engineer")
                .languages(languages);
        }
}