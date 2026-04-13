package com.raktim.fiverclone.user.DTO;

import com.raktim.fiverclone.user.model.UserRole;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record UserResponseDTO(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        String address,
        String phoneNumber,
        String firstName,
        String lastName,
        int age,
        UserRole role
) {}