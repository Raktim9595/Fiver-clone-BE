package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.sellerApplication.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerApplicationRepo;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationServiceImpl;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.user.service.UserService;
import com.raktim.fiverclone.utils.ExceptionTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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
    public void startSellerApplication_startSellerApplication_no_error() {
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

    @Test
    @DisplayName("""
            Given findByIdOrThrow, When called
            And repo returns the entity, then it should return the found entity.
            """)
    public void startSellerApplication_findByIdOrThrow_no_error() {
            UUID applicationId = UUID.randomUUID();
            SellerApplicationEntity application = new SellerApplicationEntity();
            application.setId(applicationId);

            when(repo.findById(applicationId))
                    .thenReturn(Optional.of(application));

            // Act
            SellerApplicationEntity result =
                    sellerApplicationService.findByIdOrThrow(applicationId);

            // Assert
            assertNotNull(result);
            assertEquals(applicationId, result.getId());
            assertSame(application, result);

            verify(repo).findById(applicationId);
    }

    @Test
    @DisplayName("""
            Given method findByIdOrThrow,
            And application is not found,
            Then it should return the APPLICATION_NOT_FOUND exception with proper message
            """)
    public void startSellerApplication_findByIdOrThrow_error() {
        UUID applicationId = UUID.randomUUID();

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.NOT_FOUND,
                "APPLICATION_NOT_FOUND",
                "Application with id " + applicationId + " was not found.",
                () -> sellerApplicationService.findByIdOrThrow(applicationId)
        );
    }
}
