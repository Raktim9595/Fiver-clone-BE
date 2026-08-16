package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerPortfolioRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPortfolioResponseDto;
import com.raktim.fiverclone.sellerApplication.enums.PortfolioLinkType;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.user.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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

    @BeforeEach
    public void setup() {
        UserEntity user = userTestDataSeeder.addUser();
        application = sellerApplicationTestDataSeeder.addSellerApplication(user);
    }

    @Test
    @DisplayName("""
            Given method create, When called, And there is no error,
            Then it should create the new portfolio entity and return
            """)
    public void shouldCreateSellerPortfolio() {
        SellerPortfolioRequestDto dto = new SellerPortfolioRequestDto(
                PortfolioLinkType.LINKEDIN,
                "My linked in profile page",
                "https://linkedin.com"
        );

        SellerPortfolioResponseDto result = service.create(application.getId(), dto);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(SellerPortfolioResponseDto.class);
        assertThat(result.applicationId()).isEqualTo(application.getId());
        assertThat(result.linkType()).isEqualTo(PortfolioLinkType.LINKEDIN);
        assertThat(result.title()).isEqualTo("My linked in profile page");
        assertThat(result.url()).isEqualTo("https://linkedin.com");
    }
}
