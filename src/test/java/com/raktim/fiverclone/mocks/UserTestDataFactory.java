package com.raktim.fiverclone.mocks;

import com.raktim.fiverclone.role.model.RoleEntity;
import com.raktim.fiverclone.user.DTO.UpdateUserDto;
import com.raktim.fiverclone.user.DTO.UserDTO;
import com.raktim.fiverclone.user.DTO.UserResponseDTO;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.model.UserRole;
import com.raktim.fiverclone.user.model.UserStatus;
import org.apache.catalina.User;

import java.time.LocalDate;
import java.util.UUID;

public class UserTestDataFactory {
    private static RoleEntity role =           new RoleEntity(
            UUID.fromString("01a09a99-e098-7d11-ac54-f8c1944fff66"),
            UserRole.SELLER);

    public static UserDTO.UserDTOBuilder validUserDto() {
        return UserDTO.builder()
                .username("Raktim")
                .password("123456")
                .email("raktim@gmail.com")
                .address("Australia")
                .roleId(UUID.fromString("01a09a99-e098-7a8a-83a1-6e56f9204c32"))
                .firstName("Raktim")
                .lastName("Thapa")
                .phoneNumber("0406055500")
                .timeZone("Nepal")
                .country("Nepal")
                .bio("Random")
                .status(UserStatus.ACTIVE)
                .language("English")
                .dateOfBirth(LocalDate.of(2001, 2, 25));
    }

    public static UserEntity.UserEntityBuilder validUserEntity() {
        return UserEntity.builder()
                .username("Raktim")
                .password("123456")
                .firstName("Raktim")
                .lastName("Thapa")
                .role(role)
                .email("raktim@gmail.com")
                .address("Australia")
                .phoneNumber("0406055500")
                .timeZone("Nepal")
                .country("Nepal")
                .bio("Random")
                .status(UserStatus.ACTIVE)
                .language("English")
                .dateOfBirth(LocalDate.of(2001, 2, 25));
    }

    public static UserResponseDTO.UserResponseDTOBuilder validUserResponseDTO() {
        UUID id = UUID.randomUUID();
        return UserResponseDTO.builder()
                .username("Raktim")
                .id(id)
                .email("raktim@gmail.com")
                .address("Australia")
                .role(role)
                .phoneNumber("0406055500");

    }

    public static UpdateUserDto.UpdateUserDtoBuilder validUpdateUserDto() {
        return UpdateUserDto.builder()
                .firstName("Raktim")
                .lastName("Thapa")
                .bio("Updated bio")
                .email("raktim@gmail.com")
                .country("Nepal")
                .phoneNumber("0406055500")
                .username("RaktimThapa")
                .address("Australia")
                .dateOfBirth(LocalDate.of(2004, 10, 22));
    }
}