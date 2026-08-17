package com.raktim.fiverclone.sellerApplication.service;

import com.raktim.fiverclone.sellerApplication.model.OccupationEntity;
import com.raktim.fiverclone.sellerApplication.repo.OccupationRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OccupationServiceIntegrationTest {
    @Autowired
    private OccupationService occupationService;

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

    @Test
    @DisplayName("""
            Given method findAllOccupations,
            And there is no error,
            Then it should return all the lists of occupations
            """)
    public void shouldReturnAllOccupations() {
        List<OccupationEntity> result = occupationService.findAllOccupations();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(202);
    }
}
