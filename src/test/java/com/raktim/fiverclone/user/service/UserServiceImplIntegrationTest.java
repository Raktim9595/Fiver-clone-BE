package com.raktim.fiverclone.user.service;

import com.raktim.fiverclone.mocks.UserTestDataFactory;
import com.raktim.fiverclone.user.DTO.UpdateUserDto;
import com.raktim.fiverclone.user.DTO.UserResponseDTO;
import com.raktim.fiverclone.user.UserRepo;
import com.raktim.fiverclone.user.model.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceImplIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Test
    @DisplayName("""
            Given updateUser method when called,
            And update is a success, then 
            """)
    void updateUser_shouldUpdateUserInDatabase() {
        UserEntity user = UserTestDataFactory
                .validUserEntity()
                .build();

        UserEntity savedUser = userRepo.save(user);

        UpdateUserDto updateDto = UpdateUserDto.builder()
                .firstName("Raktim")
                .lastName("Thapa")
                .address("New address")
                .phoneNumber("0498765432")
                .bio("Updated bio")
                .build();

        UserResponseDTO response = userService.updateUser(savedUser.getId(), updateDto);
        UserEntity updatedUser = userRepo.findById(savedUser.getId()).orElseThrow();

        // Assert
        assertEquals("Raktim", response.firstName());
        assertEquals("Thapa", response.lastName());

        assertEquals("Raktim", updatedUser.getFirstName());
        assertEquals("Thapa", updatedUser.getLastName());
        assertEquals("New address", updatedUser.getAddress());
        assertEquals("0498765432", updatedUser.getPhoneNumber());
        assertEquals("Updated bio", updatedUser.getBio());
    }
}
