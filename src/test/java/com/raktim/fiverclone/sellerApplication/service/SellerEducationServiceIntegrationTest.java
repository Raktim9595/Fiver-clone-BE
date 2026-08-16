package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerEducationRequestDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerEducationEntity;
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
public class SellerEducationServiceIntegrationTest {
    @Autowired
    private SellerEducationService service;

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
            Given method create, When called, And there is no error,
            Then it should create the new education entity and return
            """)
    public void shouldCreateSellerEducation() {
        SellerEducationRequestDto dto = new SellerEducationRequestDto(
                "Paschimanchal Campus",
                "Nepal",
                "Bachelors in Computer Engineering",
                "Engineering",
                2018,
                2023,
                false
        );

        SellerEducationEntity result =
                service.create(application.getId(), dto);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(SellerEducationEntity.class);
        assertThat(result.getApplication().getId()).isEqualTo(application.getId());
        assertThat(result.getCurrent()).isFalse();
        assertThat(result.getDegree()).isEqualTo("Bachelors in Computer Engineering");
    }
}
