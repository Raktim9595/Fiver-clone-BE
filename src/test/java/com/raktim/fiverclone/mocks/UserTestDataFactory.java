package com.raktim.fiverclone.mocks;

import com.raktim.fiverclone.user.DTO.UserDTO;
import com.raktim.fiverclone.user.DTO.UserResponseDTO;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.model.UserRole;
import org.apache.catalina.User;

import java.util.UUID;

public class UserTestDataFactory {
    public static UserDTO.UserDTOBuilder validUserDto() {
        return UserDTO.builder()
                .username("Raktim")
                .password("123456")
                .email("raktim@gmail.com")
                .age(20)
                .address("Australia")
                .role(UserRole.BUYER)
                .firstName("Raktim")
                .lastName("Thapa")
                .phoneNumber("0406055500");
    }

    public static UserEntity.UserEntityBuilder validUserEntity() {
        return UserEntity.builder()
                .username("Raktim")
                .password("123456")
                .firstName("Raktim")
                .lastName("Thapa")
                .role(UserRole.SELLER)
                .email("raktim@gmail.com")
                .age(20)
                .address("Australia")
                .phoneNumber("0406055500");
    }

    public static UserResponseDTO.UserResponseDTOBuilder validUserResponseDTO() {
        UUID id = UUID.randomUUID();
        return UserResponseDTO.builder()
                .username("Raktim")
                .id(id)
                .email("raktim@gmail.com")
                .address("Australia")
                .age(20)
                .role(UserRole.SELLER)
                .phoneNumber("0406055500");

    }
}