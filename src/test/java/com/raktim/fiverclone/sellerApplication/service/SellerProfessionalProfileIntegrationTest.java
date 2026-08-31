package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerProfessionalProfileRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerProfessionalProfileResponseDto;
import com.raktim.fiverclone.sellerApplication.enums.SellerOnboardingSteps;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.user.model.UserEntity;
import com.raktim.fiverclone.utils.ExceptionTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(IntegrationTestConfig.class)
public class SellerProfessionalProfileIntegrationTest {
    @Autowired
    private SellerApplicationTestDataSeeder sellerApplicationTestDataSeeder;

    @Autowired
    private UserTestDataSeeder userTestDataSeeder;

    @Autowired
    private SellerProfessionalProfileService service;

    private UserEntity user;
    private SellerProfessionalProfileRequestDto dto;
    private SellerApplicationEntity application;

    @BeforeEach
    public void setUp() {
        user = userTestDataSeeder.addUser();
        application = sellerApplicationTestDataSeeder.addSellerApplication(
                user,
                SellerApplicationEntity.builder()
                        .currentStep(SellerOnboardingSteps.PROFESSIONAL_PROFILE)
        );
        dto = SellerApplicationTestData.validSellerProfessionalProfileRequestDto().build();
    }

    @Test
    @DisplayName("""
            Given method createProfessionalProfile, When called,
            And there is no error,
            Then it should successfully create the professional profile of seller and update the application state
            """)
    public void shouldCreateSellerProfessionalProfile() {
        SellerProfessionalProfileResponseDto result = service.createProfessionalProfile(
                application.getId(),
                dto
        );

        SellerApplicationEntity updatedApplication = sellerApplicationTestDataSeeder
                .getApplication(application.getId());

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(SellerProfessionalProfileResponseDto.class);
        assertSame(result.professionalLevel(), dto.professionalLevel());
        assertSame(100, updatedApplication.getCompletionPercentage());
        assertSame(SellerOnboardingSteps.REVIEW, updatedApplication.getCurrentStep());
        assertSame(result.applicationId(), updatedApplication.getId());
    }

    @Test
    @DisplayName("""
            Given method createProfessionalProfile, When called,
            And there is error since application personal profile is not updated,
            """)
    public void shouldThrowForbiddenException() {
        SellerApplicationEntity newApplication = sellerApplicationTestDataSeeder.addSellerApplication(
                user
        );

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.FORBIDDEN,
                "INVALID_ONBOARDING_STEP",
                "This action can be performed only when professional profile is active.",
                () -> service.createProfessionalProfile(newApplication.getId(), dto)
        );

        SellerApplicationEntity foundApplication = sellerApplicationTestDataSeeder
                .getApplication(newApplication.getId());

        assertThat(foundApplication).isNotNull();
        assertSame(SellerOnboardingSteps.PERSONAL_PROFILE, foundApplication.getCurrentStep());
        assertSame(0, foundApplication.getCompletionPercentage());
    }

    @Test
    @DisplayName("""
            Given update method, When called,
            And there is no error,
            Then it successfully update the professional profile
            """)
    public void shouldUpdateSellerProfessionalProfile() {
        SellerProfessionalProfileResponseDto createResult =
                service.createProfessionalProfile(application.getId(), dto);

        // set this to review again otherwise it fails
        application.setCurrentStep(SellerOnboardingSteps.REVIEW);

        SellerProfessionalProfileRequestDto updatedDto = SellerApplicationTestData
                .validSellerProfessionalProfileRequestDto()
                .yearsOfExperience(10)
                .occupationId(UUID.fromString("f42b5925-404f-4156-84ef-e45b7d528f40"))
                .build();

        SellerProfessionalProfileResponseDto updateResult = service.update(
                createResult.id(), application.getId(), updatedDto
        );

        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isInstanceOf(SellerProfessionalProfileResponseDto.class);
        assertThat(updateResult.yearsOfExperience()).isEqualTo(10);
        assertThat(updateResult.occupation()).isEqualTo("Event Planner");
    }

    @Test
    @DisplayName("""
            Given update method, When called,
            And there is an error since the profile belongs to different application,
            Then it should throw proper error message with error code
            """)
    public void shouldThrowForbiddenExceptionWhenUpdateProfessionalProfile() {
        SellerApplicationEntity newApplication = sellerApplicationTestDataSeeder.addSellerApplication(user);
        SellerProfessionalProfileResponseDto createResult = service
                .createProfessionalProfile(application.getId(), dto);

        newApplication.setCurrentStep(SellerOnboardingSteps.REVIEW);
        SellerProfessionalProfileRequestDto updatedDto = SellerApplicationTestData
                .validSellerProfessionalProfileRequestDto()
                .yearsOfExperience(10)
                .occupationId(UUID.fromString("f42b5925-404f-4156-84ef-e45b7d528f40"))
                .build();

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.FORBIDDEN,
                "SELLER_PROFESSIONAL_PROFILE_NOT_FOUND",
                "Seller professional profile mismatched found for this application.",
                () -> service.update(createResult.id(), newApplication.getId() , updatedDto)
        );
    }
}
