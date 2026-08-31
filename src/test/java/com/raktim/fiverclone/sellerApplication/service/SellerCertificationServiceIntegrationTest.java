package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerCertificationRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerCertificationResponseDto;
import com.raktim.fiverclone.sellerApplication.enums.SellerOnboardingSteps;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerCertificationEntity;
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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Import(IntegrationTestConfig.class)
public class SellerCertificationServiceIntegrationTest {
    @Autowired
    private SellerCertificationService service;

    @Autowired
    private SellerApplicationTestDataSeeder sellerApplicationTestDataSeeder;

    @Autowired
    private UserTestDataSeeder userTestDataSeeder;

    private SellerApplicationEntity application;
    private UserEntity user;
    private SellerCertificationRequestDto dto;

    @BeforeEach
    public void setup() {
        user = userTestDataSeeder.addUser();
        application = sellerApplicationTestDataSeeder.addSellerApplication(user);
        dto = SellerApplicationTestData.validSellerCertificationRequestDto().build();
    }

    @Test
    @DisplayName("""
            Given method create, When called, And there is no error,
            Then it should create the new certification entity and return
            """)
    public void shouldCreateSellerCertification() {
        SellerCertificationResponseDto result =
                service.create(application.getId(), dto);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(SellerCertificationResponseDto.class);
        assertThat(result.applicationId()).isEqualTo(application.getId());
        assertThat(result.certificationName()).isEqualTo("AWS cloud hero");
    }

    @Test
    @DisplayName("""
            Given method update when called, And there is no error,
            Then it should successfully update the seller certifications and return proper response
            """)
    public void shouldUpdateSellerCertification() {
        SellerCertificationResponseDto createResult = service.create(application.getId(), dto);

        SellerCertificationRequestDto updateDto = SellerApplicationTestData
                .validSellerCertificationRequestDto()
                .certificationName("Just a random thing")
                .issuingOrganization("High as dope")
                .build();

        application.setCurrentStep(SellerOnboardingSteps.REVIEW);

        SellerCertificationResponseDto updateResult = service
                .update(createResult.id(), application.getId(), updateDto);

        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isInstanceOf(SellerCertificationResponseDto.class);
        assertThat(updateResult.applicationId()).isEqualTo(application.getId());
        assertThat(updateResult.certificationName()).isEqualTo("Just a random thing");
        assertThat(updateResult.issuingOrganization()).isEqualTo("High as dope");
    }

    @Test
    @DisplayName("""
            Given method update, When called,
            And there is an error due to certification owned by different application,
            Then it should return proper exception
            """)
    public void shouldThrowErrorOnUpdateSellerCertification() {
        SellerCertificationResponseDto createResult = service.create(application.getId(), dto);
        SellerApplicationEntity newApplication = sellerApplicationTestDataSeeder.addSellerApplication(user);

        newApplication.setCurrentStep(SellerOnboardingSteps.REVIEW);
        SellerCertificationRequestDto updateDto = SellerApplicationTestData
                .validSellerCertificationRequestDto()
                .certificationName("Just a random thing")
                .issuingOrganization("High as dope")
                .build();

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.FORBIDDEN,
                "MISMATCH_SELLER_CERTIFICATION_AND_APPLICATION",
                "Seller certification details mismatched found for this application.",
                () -> service.update(createResult.id(), newApplication.getId(), updateDto)
        );
    }

    @Test
    @DisplayName("""
            Given method delete, When called
            And there is no error,
            Then it should successfully delete and return proper message
            """)
    public void shouldDelete() {
        SellerCertificationResponseDto createResult = service.create(application.getId(), dto);
        application.setCurrentStep(SellerOnboardingSteps.REVIEW);

        String result = service.delete(createResult.id(), application.getId());
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo("Successfully deleted Seller Certification with applicationId %s and certificationId %s"
                .formatted(application.getId(), createResult.id()));

        SellerCertificationEntity certificationEntity = service.findById(createResult.id());
        assertThat(certificationEntity).isNull();
        SellerApplicationEntity foundApplication = sellerApplicationTestDataSeeder.getApplication(application.getId());
        assertThat(foundApplication).isNotNull();
    }
}
