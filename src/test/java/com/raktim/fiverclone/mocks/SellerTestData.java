package com.raktim.fiverclone.mocks;

import com.raktim.fiverclone.seller.dto.SellerDto;

import java.util.Set;
import java.util.UUID;

public class SellerTestData {
    public static SellerDto.SellerDtoBuilder validSellerDto() {
        return SellerDto.builder()
                .userId(UUID.randomUUID())
                .description("Java Developer")
                .experienceId(UUID.randomUUID())
                .skills(Set.of(UUID.randomUUID(), UUID.randomUUID()));
    }
}
