package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerPortfolioRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPortfolioResponseDto;
import com.raktim.fiverclone.sellerApplication.enums.PortfolioLinkType;
import com.raktim.fiverclone.sellerApplication.enums.SellerOnboardingSteps;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerPortfolioEntity;
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
public class SellerPortfolioServiceIntegrationTest {
    @Autowired
    private SellerPortfolioService service;

    @Autowired
    private SellerApplicationTestDataSeeder sellerApplicationTestDataSeeder;

    @Autowired
    private UserTestDataSeeder userTestDataSeeder;

    private SellerApplicationEntity application;
    private SellerPortfolioRequestDto dto;
    private UserEntity user;

    @BeforeEach
    public void setup() {
        user = userTestDataSeeder.addUser();
        application = sellerApplicationTestDataSeeder.addSellerApplication(user);
        dto = SellerApplicationTestData.validSellerPortfolioRequestDto().build();
    }

    @Test
    @DisplayName("""
            Given method create, When called, And there is no error,
            Then it should create the new portfolio entity and return
            """)
    public void shouldCreateSellerPortfolio() {
        SellerPortfolioResponseDto result = service.create(application.getId(), dto);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(SellerPortfolioResponseDto.class);
        assertThat(result.applicationId()).isEqualTo(application.getId());
        assertThat(result.linkType()).isEqualTo(PortfolioLinkType.LINKEDIN);
        assertThat(result.title()).isEqualTo("My linked in profile page");
        assertThat(result.url()).isEqualTo("https://linkedin.com");
    }

    @Test
    @DisplayName("""
            Given method update, When called, And there is no error,
            Then it should update the portfolio and return proper response
            """)
    public void shouldUpdateSellerPortfolio() {
        SellerPortfolioResponseDto createResult = service.create(application.getId(), dto);

        application.setCurrentStep(SellerOnboardingSteps.REVIEW);

        SellerPortfolioRequestDto updateDto = SellerApplicationTestData
                .validSellerPortfolioRequestDto()
                .linkType(PortfolioLinkType.GITHUB)
                .title("My github page")
                .build();

        SellerPortfolioResponseDto updateResult = service.update(
                createResult.id(), application.getId(), updateDto
        );

        assertThat(updateResult).isNotNull();
        assertThat(updateResult).isInstanceOf(SellerPortfolioResponseDto.class);
        assertThat(updateResult.applicationId()).isEqualTo(application.getId());
        assertThat(updateResult.linkType()).isEqualTo(PortfolioLinkType.GITHUB);
        assertThat(updateResult.title()).isEqualTo("My github page");
    }

    @Test
    @DisplayName("""
            Given method update, When called,
            And there is an error due to the profile belongs to different application than the passed one
            """)
    public void shouldThrowErrorOnUpdateIfProfileBelongToDifferentApplication() {
        SellerPortfolioResponseDto createResult = service.create(application.getId(), dto);
        SellerApplicationEntity newApplication = sellerApplicationTestDataSeeder.addSellerApplication(user);
        newApplication.setCurrentStep(SellerOnboardingSteps.REVIEW);

        SellerPortfolioRequestDto updateDto = SellerApplicationTestData
                .validSellerPortfolioRequestDto()
                .linkType(PortfolioLinkType.GITHUB)
                .title("My github page")
                .build();

        ExceptionTestUtil.assertBusinessException(
                HttpStatus.FORBIDDEN,
                "MISMATCH_SELLER_PORTFOLIO_AND_APPLICATION",
                "Seller Portfolio details mismatched found for this application.",
                () -> service.update(
                        createResult.id(), newApplication.getId(), updateDto
                )
        );
    }

    @Test
    @DisplayName("""
            Give method delete, When called, And there is no error,
            Then it should successfully delete the seller portfolio details
            """)
    public void shouldDeleteSellerPortfolio() {
        SellerPortfolioResponseDto createResult = service.create(application.getId(), dto);
        application.setCurrentStep(SellerOnboardingSteps.REVIEW);
        String deleteResult = service.delete(createResult.id(), application.getId());

        assertThat(deleteResult).isNotNull();
        assertThat(deleteResult).isEqualTo("Successfully deleted seller portfolio with id=%s for application id = %s"
                .formatted(createResult.id(), application.getId()));

        SellerPortfolioEntity sellerPortfolio = service.findById(createResult.id());
        assertThat(sellerPortfolio).isNull();
    }
}
