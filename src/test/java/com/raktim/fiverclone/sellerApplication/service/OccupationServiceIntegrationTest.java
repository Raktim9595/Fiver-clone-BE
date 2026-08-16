package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.sellerApplication.model.OccupationEntity;
import com.raktim.fiverclone.sellerApplication.repo.OccupationRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OccupationServiceIntegrationTest {
    @Autowired
    private OccupationService occupationService;

    @Autowired
    private OccupationRepo occupationRepo;

    @Test
    @DisplayName("""
            Given createOccupation when called,
            And there is no error,
            Then it should create the occupation and return the entity
            """)
    public void shouldCreateOccupation() {
        OccupationEntity result = occupationService.createOccupation("Software Engineer");
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Software Engineer");
    }
}
