package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerEducationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerEducationResponseDto;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(IntegrationTestConfig.class)
public class SellerEducationServiceIntegrationTest {
    @Autowired
    private SellerEducationService service;

    @Autowired
    private SellerApplicationTestDataSeeder sellerApplicationTestDataSeeder;

    @Autowired
    private UserTestDataSeeder userTestDataSeeder;

    private SellerApplicationEntity application;
    private UserEntity user;
    private  SellerEducationRequestDto dto;

    @BeforeEach
    public void setup() {
        user = userTestDataSeeder.addUser();
        application = sellerApplicationTestDataSeeder.addSellerApplication(user);
        dto = SellerApplicationTestData
                .validSellerEducationRequestDto().build();
    }

    @Test
    @DisplayName("""
            Given method create, When called, And there is no error,
            Then it should create the new education entity and return
            """)
    public void shouldCreateSellerEducation() {
        SellerEducationResponseDto result =
                service.create(application.getId(), dto);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(SellerEducationResponseDto.class);
        assertThat(result.applicationId()).isEqualTo(application.getId());
        assertThat(result.current()).isFalse();
        assertThat(result.degree()).isEqualTo("Bachelors");
    }

    @Test
    @DisplayName("""
            Give update method, When called,
            And there is no error,
            Then it should correctly update the seller education details
            """)
    public void shouldUpdateSellerEducation() {
        SellerEducationResponseDto createResult =
                service.create(application.getId(), dto);

        SellerEducationRequestDto updateDto =
                SellerApplicationTestData
                        .validSellerEducationRequestDto()
                        .degree("Masters")
                        .institutionName("WRC")
                        .country("Antartica")
                        .build();

        application.setCurrentStep(SellerOnboardingSteps.REVIEW);
        SellerEducationResponseDto updateResult = service.update(
                createResult.id(), application.getId(), updateDto
        );

        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isInstanceOf(SellerEducationResponseDto.class);
        assertThat(updateResult.applicationId()).isEqualTo(application.getId());
        assertThat(updateResult.country()).isEqualTo("Antartica");
        assertThat(updateResult.degree()).isEqualTo("Masters");
        assertThat(updateResult.institutionName()).isEqualTo("WRC");
    }

    @Test
    @DisplayName("""
            Given update method, When called,
            And there is an error of sellerEducation and application ownership mismatch,
            Then it should display proper error
            """)
    public void shouldFailSellerEducationOnApplicationMismatch() {
        SellerApplicationEntity newApplication = sellerApplicationTestDataSeeder.addSellerApplication(user);
        SellerEducationResponseDto createResult =
                service.create(application.getId(), dto);

        SellerEducationRequestDto updateDto =
                SellerApplicationTestData
                        .validSellerEducationRequestDto()
                        .degree("Masters")
                        .institutionName("WRC")
                        .country("Antartica")
                        .build();

        newApplication.setCurrentStep(SellerOnboardingSteps.REVIEW);

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.FORBIDDEN,
                "MISMATCH_SELLER_EDUCATION_AND_APPLICATION",
                "Seller education details mismatched found for this application.",
                () -> service.update(createResult.id(), newApplication.getId(), updateDto)
        );
    }
}
