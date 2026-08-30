package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.language.model.LanguageEntity;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileResponseDto;
import com.raktim.fiverclone.sellerApplication.enums.SellerApplicationStatus;
import com.raktim.fiverclone.sellerApplication.enums.SellerOnboardingSteps;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.service.sellerPersonalProfile.SellerPersonalProfileService;
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

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(IntegrationTestConfig.class)
public class SellerPersonalProfileServiceIntegrationTest {
    @Autowired
    private SellerPersonalProfileService service;

    @Autowired
    private SellerApplicationTestDataSeeder sellerApplicationTestDataSeeder;

    @Autowired
    private UserTestDataSeeder userTestDataSeeder;

    private SellerApplicationEntity application;
    private UserEntity user;
    private SellerPersonalProfileRequestDto dto;
    private List<LanguageEntity> languages;

    @BeforeEach
    public void setup() {
        user = userTestDataSeeder.addUser();
        application = sellerApplicationTestDataSeeder.addSellerApplication(user);
        languages = userTestDataSeeder.findAllLanguages();
        dto = SellerApplicationTestData
                .validSellerPersonalProfileRequestDto()
                .languages(Set.of(languages.getFirst().getId(), languages.getLast().getId()))
                .build();
    }

    @Test
    @DisplayName("""
            Given create method, When called,
            And there is no error,
            Then it should successfully create the seller personal profile and return
            """)
    public void shouldCreateSellerPersonalProfile() {
        SellerPersonalProfileResponseDto result = service.create(application.getId(), dto);
        SellerApplicationEntity currentApplication =
                sellerApplicationTestDataSeeder.getApplication(application.getId());

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(SellerPersonalProfileResponseDto.class);
        assertThat(result.displayName()).isEqualTo("alanwalker");
        assertThat(result.description()).isEqualTo("I am motivated software engineer");
        assertThat(result.languages()).isEqualTo(
                Set.of(languages.getFirst().getLanguage(), languages.getLast().getLanguage())
        );

        assertThat(currentApplication.getCurrentStep()).isEqualTo(
                SellerOnboardingSteps.PROFESSIONAL_PROFILE
        );
        assertThat(currentApplication.getCompletionPercentage()).isEqualTo(50);
        assertThat(currentApplication.getId()).isEqualTo(application.getId());
    }

    @Test
    @DisplayName("""
            Given create method, When called,
            And application is not editable i.e. it's status is not DRAFT,
            Then it should throw exception
            """)
    public void shouldThrowException() {
        SellerApplicationEntity newApplication = sellerApplicationTestDataSeeder.addSellerApplication(
                user,
                SellerApplicationEntity.builder().status(SellerApplicationStatus.APPROVED)
        );

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.FORBIDDEN,
                "EDITING_NOT_ALLOWED",
                "This seller application can only be edited while it is in DRAFT status.",
                () -> service.create(newApplication.getId(), dto)
        );

        SellerApplicationEntity currentApplication =
                sellerApplicationTestDataSeeder.getApplication(newApplication.getId());
        assertThat(currentApplication.getCurrentStep()).isEqualTo(
                SellerOnboardingSteps.PERSONAL_PROFILE
        );
        assertThat(currentApplication.getCompletionPercentage()).isEqualTo(0);
    }

    @Test
    @DisplayName("""
            Given method update, When called,
            And there is no error,
            Then it should successfully update the personal profile of the seller and return proper response.
            """)
    public void shouldUpdateSellerPersonalProfile() {
        SellerPersonalProfileResponseDto createResult = service.create(application.getId(), dto);

        // set this to review again otherwise it fails
        application.setCurrentStep(SellerOnboardingSteps.REVIEW);

        SellerPersonalProfileRequestDto updatedDto =
                SellerApplicationTestData
                        .validSellerPersonalProfileRequestDto()
                        .displayName("updated name")
                        .description("updated description")
                        .languages(Set.of(
                                languages.getFirst().getId(),
                                languages.getLast().getId(),
                                UUID.fromString("698ae069-a136-4c88-9dd9-2dbdf0b6babd")
                        ))
                        .build();

        SellerPersonalProfileResponseDto updateResult =
                service.update(application.getId(), createResult.id(), updatedDto);

        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isInstanceOf(SellerPersonalProfileResponseDto.class);
        assertThat(updateResult.displayName()).isEqualTo("updated name");
        assertThat(updateResult.description()).isEqualTo("updated description");
        assertThat(updateResult.languages()).contains(
                languages.getFirst().getLanguage(),
                languages.getLast().getLanguage(),
                "Thai"
        );
    }

    @Test
    @DisplayName("""
            Given method update, when called,
            And it throws exception because the application currentStep is not in REVIEW,
            Then it should throw proper error message and error code
            """)
    public void shouldThrowErrorOnUpdate() {
        SellerPersonalProfileResponseDto createResult = service.create(application.getId(), dto);
        SellerPersonalProfileRequestDto updatedDto =
                SellerApplicationTestData
                        .validSellerPersonalProfileRequestDto()
                        .displayName("updated name")
                        .description("updated description")
                        .languages(Set.of(
                                languages.getFirst().getId(),
                                languages.getLast().getId(),
                                UUID.fromString("698ae069-a136-4c88-9dd9-2dbdf0b6babd")
                        ))
                        .build();

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.FORBIDDEN,
                "INVALID_STEP_FOR_EDIT",
                "This action can be performed only when the application current step is in REVIEW.",
                () -> service.update(application.getId(), createResult.id(), updatedDto)
        );
    }

    @Test
    @DisplayName("""
            Given method update, when called,
            And it throws exception because the profile belongs to different application,
            Then it should throw proper error message and error code
            """)
    public void shouldThrowErrorOnUpdateDueToApplicationMismatch() {
        SellerApplicationEntity newApplication = sellerApplicationTestDataSeeder.addSellerApplication(user);

        SellerPersonalProfileResponseDto createResult = service.create(application.getId(), dto);

        newApplication.setCurrentStep(SellerOnboardingSteps.REVIEW);
        SellerPersonalProfileRequestDto updatedDto =
                SellerApplicationTestData
                        .validSellerPersonalProfileRequestDto()
                        .displayName("updated name")
                        .description("updated description")
                        .languages(Set.of(
                                languages.getFirst().getId(),
                                languages.getLast().getId(),
                                UUID.fromString("698ae069-a136-4c88-9dd9-2dbdf0b6babd")
                        ))
                        .build();

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.FORBIDDEN,
                "SELLER_PERSONAL_PROFILE_NOT_FOUND",
                "Seller personal profile mismatched found for this application.",
                () -> service.update(newApplication.getId(), createResult.id(), updatedDto)
        );
    }
}
