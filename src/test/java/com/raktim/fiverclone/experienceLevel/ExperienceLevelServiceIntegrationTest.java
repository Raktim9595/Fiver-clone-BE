package com.raktim.fiverclone.experienceLevel;

import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevel;
import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevelEntity;
import com.raktim.fiverclone.seeds.experienceLevel.ExperienceLevelService;
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
public class ExperienceLevelServiceIntegrationTest {
    @Autowired
    private ExperienceLevelService experienceLevelService;

    @Test
    @DisplayName("""
            Given getAllExperienceLevels, When called
            Then it should return the list of experienceLevel entities available
            """)
    public void test_getAllExperienceLevels() {
        List<ExperienceLevelEntity> result = experienceLevelService.getExperienceLevels();

        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(ExperienceLevelEntity::getExperienceLevel)
                .containsExactlyInAnyOrder(ExperienceLevel.BEGINNER, ExperienceLevel.INTERMEDIATE, ExperienceLevel.PRO);
    }
}
