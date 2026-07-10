package com.raktim.fiverclone.country;

import com.raktim.fiverclone.country.model.CountryEntity;
import com.raktim.fiverclone.country.repo.CountryRepo;
import com.raktim.fiverclone.country.service.CountryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CountryServiceIntegrationTest {
    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepo countryRepo;

    @Test
    @DisplayName("""
            Given findAll when called
            Then it should return proper result and entity 
            """)
    public void test_findAll() {
        countryRepo.deleteAll();

        int countriesCount = countryRepo.findAll().size();
        assertEquals(0, countriesCount);

        CountryEntity australia = CountryEntity.builder()
                .id(UUID.randomUUID())
                .name("Australia")
                .phoneCode("+61")
                .build();

        CountryEntity nepal = CountryEntity.builder()
                .id(UUID.randomUUID())
                .name("Nepal")
                .phoneCode("+977")
                .build();

        countryRepo.saveAll(List.of(australia, nepal));

        // When
        List<CountryEntity> result = countryService.findAll();

        // Then
        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(CountryEntity::getName)
                .containsExactlyInAnyOrder("Australia", "Nepal");

        assertThat(result)
                .extracting(CountryEntity::getPhoneCode)
                .containsExactlyInAnyOrder("+61", "+977");

        assertThat(result)
                .extracting(CountryEntity::getId)
                .doesNotContainNull();
    }
}
