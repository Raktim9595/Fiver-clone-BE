package com.raktim.fiverclone.user.DTO;

import com.raktim.fiverclone.user.model.UserRole;
import com.raktim.fiverclone.user.model.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record UpdateUserDto(
        @NotBlank(message = "FirstName should not be empty")
        String firstName,

        @NotBlank(message = "LastName should not be empty")
        String lastName,

        @NotBlank(message = "LastName should not be empty")
        String username,

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
) {

}
