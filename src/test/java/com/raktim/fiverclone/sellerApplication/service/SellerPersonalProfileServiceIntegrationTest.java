package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.mocks.SellerApplicationTestData;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileRequestDto;
import com.raktim.fiverclone.sellerApplication.dto.SellerPersonalProfileResponseDto;
import com.raktim.fiverclone.sellerApplication.enums.SellerOnboardingSteps;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.service.sellerPersonalProfile.SellerPersonalProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

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

    @BeforeEach
    public void setup() {
        var user = userTestDataSeeder.addUser();
        application = sellerApplicationTestDataSeeder.addSellerApplication(user);
    }

    @Test
    @DisplayName("""
            Given create method, When called,
            And there is no error,
            Then it should successfully create the seller personal profile and return
            """)
    public void shouldCreateSellerPersonalProfile() {
        var languages = userTestDataSeeder.findAllLanguages();
        SellerPersonalProfileRequestDto dto = SellerApplicationTestData
                .validSellerPersonalProfileRequestDto()
                .languages(Set.of(languages.getFirst().getId(), languages.getLast().getId()))
                .build();

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
                SellerOnboardingSteps.PERSONAL_PROFILE
        );
        assertThat(currentApplication.getCompletionPercentage()).isEqualTo(50);
        assertThat(currentApplication.getId()).isEqualTo(application.getId());
    }
}
