package com.raktim.fiverclone.seller.service;

import com.raktim.fiverclone.seller.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.seller.model.SellerApplicationEntity;
import com.raktim.fiverclone.seller.repo.SellerApplicationRepo;
import com.raktim.fiverclone.seller.service.sellerApplication.SellerApplicationServiceImpl;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SellerApplicationServiceTest {
    @Mock
    private SellerApplicationRepo repo;

    @Mock
    private UserService userService;

    @InjectMocks
    private SellerApplicationServiceImpl sellerApplicationService;

    @BeforeEach
    public void setup() {
        UserEntity user = UserEntity.builder().build();
    }

    @Test
    @DisplayName("""
            SellerApplicationService Unit Test,
            When a valid dto is passed,
            Then it should create the seller and return the respective entity
            """)
    public void startSellerApplication() {
        UUID userId = UUID.randomUUID();
        UserEntity user = UserEntity.builder().build();
        user.setId(userId);

        StartSellerApplicationRequestDto request =
                new StartSellerApplicationRequestDto(userId);

        when(userService.findUserByIdOrThrow(userId)).thenReturn(user);

        // Act
        SellerApplicationEntity result =
                sellerApplicationService.startSellerApplication(request);

        // Assert
        assertNotNull(result);
        assertEquals(user, result.getUser());

        verify(userService, times(1))
                .findUserByIdOrThrow(userId);

        verify(repo, times(1))
                .save(any(SellerApplicationEntity.class));
    }

}
