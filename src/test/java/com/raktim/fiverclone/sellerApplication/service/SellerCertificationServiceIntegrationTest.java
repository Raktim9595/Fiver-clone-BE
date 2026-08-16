package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.common.IntegrationTestConfig;
import com.raktim.fiverclone.seeder.SellerApplicationTestDataSeeder;
import com.raktim.fiverclone.seeder.UserTestDataSeeder;
import com.raktim.fiverclone.sellerApplication.dto.SellerCertificationRequestDto;
import com.raktim.fiverclone.sellerApplication.model.SellerApplicationEntity;
import com.raktim.fiverclone.sellerApplication.model.SellerCertificationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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

    @BeforeEach
    public void setup() {
        var user = userTestDataSeeder.addUser();
        application = sellerApplicationTestDataSeeder.addSellerApplication(user);
    }

    @Test
    @DisplayName("""
            Given method create, When called, And there is no error,
            Then it should create the new certification entity and return
            """)
    public void shouldCreateSellerCertification() {
        SellerCertificationRequestDto dto = new SellerCertificationRequestDto(
                "AWS cloud hero",
                "Amazon Web Services",
                LocalDate.now(),
                LocalDate.now(),
                "aws-id",
                "https://credentials"
        );

        SellerCertificationEntity result =
                service.create(application.getId(), dto);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(SellerCertificationEntity.class);
        assertThat(result.getApplication().getId()).isEqualTo(application.getId());
        assertThat(result.getCertificationName()).isEqualTo("AWS cloud hero");
    }
}
