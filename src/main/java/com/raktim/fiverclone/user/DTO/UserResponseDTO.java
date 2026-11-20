package com.raktim.fiverclone.user.DTO;

import com.raktim.fiverclone.role.model.RoleEntity;
import com.raktim.fiverclone.user.model.UserRole;
import com.raktim.fiverclone.user.model.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
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
        RoleEntity role,
        String bio,
        String timeZone,
        String country,
        String language,
        LocalDate dateOfBirth,
        UserStatus status
) {}