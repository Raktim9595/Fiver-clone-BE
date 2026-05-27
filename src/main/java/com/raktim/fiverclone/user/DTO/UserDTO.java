package com.raktim.fiverclone.user.DTO;

import com.raktim.fiverclone.user.model.UserRole;
import com.raktim.fiverclone.user.model.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record UserDTO(
        @NotBlank(message = "FirstName should not be empty")
        String firstName,

        @NotBlank(message = "LastName should not be empty")
        String lastName,

        @NotBlank(message = "LastName should not be empty")
        String username,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^0[0-9]{9}$", message = "Invalid Australian phone number")
        String phoneNumber,

        @NotNull(message = "Role is required")
        UserRole role,

        String bio,
        String timeZone,
        String country,
        String language,

        @NotNull(message = "Date of Birth is required")
        LocalDate dateOfBirth,

        UserStatus status
) {}