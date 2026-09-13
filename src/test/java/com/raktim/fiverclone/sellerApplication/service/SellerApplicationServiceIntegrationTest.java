package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.StartSellerApplicationRequestDto;
import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.repo.SellerApplicationRepo;
import com.raktim.fiverclone.sellerApplication.service.sellerApplication.SellerApplicationService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(IntegrationTestConfig.class)
public class SellerApplicationServiceIntegrationTest {
    @Autowired
    private SellerApplicationService sellerApplicationService;

    @Autowired
    private SellerApplicationTestDataSeeder sellerApplicationTestDataSeeder;

    @Autowired
    private UserTestDataSeeder userTestDataSeeder;

    private UserEntity user;
    private SellerApplicationEntity application;

    @BeforeEach
    public void setup() {
        user = userTestDataSeeder.addUser();
        application = sellerApplicationTestDataSeeder.addSellerApplication(user);
    }

    @Test
    @DisplayName("""
            SellerApplicationService Integration Test,
            When a valid dto is passed,
            Then it should create the seller and return the respective entity
            """)
    public void startSellerApplication_startSellerApplication_no_error() {
        StartSellerApplicationRequestDto request =
                new StartSellerApplicationRequestDto(user.getId());

        // Act
        SellerApplicationEntity result =
                sellerApplicationService.startSellerApplication(request);

        // Assert
        assertNotNull(result);
        assertNotEquals(result.getId(), application.getId());
        assertEquals(user, result.getUser());
    }

    @Test
    @DisplayName("""
            Given findByIdOrThrow, When called
            And repo returns the entity, then it should return the found entity.
            """)
    public void startSellerApplication_findByIdOrThrow_no_error() {
            SellerApplicationEntity result =
                    sellerApplicationService.findByIdOrThrow(application.getId());
            assertNotNull(result);
            assertSame(application, result);
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

    @Test
    @DisplayName("""
            Given method completeSellerApplication, When called,
            And there is no error,
            Then it should successfully change the status of the application
            """)
    public void submitSellerApplication_withNoError() {
        SellerApplicationEntity result = sellerApplicationService.completeSellerApplication(
                application.getId(),
                user.getId()
        );

        assertNotNull(result);
        assertEquals(SellerApplicationStatus.SUBMITTED, result.getStatus());
        assertNotNull(result.getSubmittedAt());
    }

    @Test
    @DisplayName("""
            Given method completeSellerApplication, When called,
            And different userId is passed instead of application userId, i.e different people owns the application,
            Then it should throw error and return proper message
            """)
    public void submitSellerApplication_with_error() {
        UUID userId = UUID.randomUUID();
        ExceptionTestUtil.assertBusinessException(
              HttpStatus.UNAUTHORIZED,
              "INVALID_USER_ID",
              "The provided application belongs to different user so can't make any changes.",
              () -> sellerApplicationService.completeSellerApplication(application.getId(), userId)
        );
    }
}
