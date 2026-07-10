package com.raktim.fiverclone.language;

import com.raktim.fiverclone.language.dto.LanguageDto;
import com.raktim.fiverclone.language.model.LanguageEntity;
import com.raktim.fiverclone.language.repo.LanguageRepo;
import com.raktim.fiverclone.language.service.LanguageService;
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
public class LanguageServiceIntegrationTest {
    @Autowired
    private LanguageService languageService;

    @Autowired
    private LanguageRepo languageRepo;

    @Test
    @DisplayName("""
            Given findAll When called
            Then it should return proper result and proper entity
            """)
    void findAll_shouldReturnAllLanguages() {
        languageRepo.deleteAll();

        // confirm that db get emptied
        int current_languages =  languageService.findAll().size();
        assertThat(current_languages).isEqualTo(0);

        LanguageEntity english = LanguageEntity.builder()
                .code("en")
                .language("English")
                .build();

        LanguageEntity nepali = LanguageEntity.builder()
                .code("ne")
                .language("Nepali")
                .build();

        languageRepo.saveAll(List.of(english, nepali));

        List<LanguageDto> result = languageService.findAll();

        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(LanguageDto::code)
                .containsExactlyInAnyOrder("en", "ne");

        assertThat(result)
                .extracting(LanguageDto::language)
                .containsExactlyInAnyOrder("English", "Nepali");

        assertThat(result)
                .allSatisfy(language -> assertThat(language.id()).isNotNull());
    }
}
