package com.raktim.fiverclone.skills;

import com.raktim.fiverclone.seeds.skills.Skill;
import com.raktim.fiverclone.seeds.skills.SkillEntity;
import com.raktim.fiverclone.seeds.skills.SkillsService;
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
public class SkillServiceIntegrationTest {
    @Autowired
    private SkillsService skillsService;

    @Test
    public void test_findAll() {
        List<SkillEntity> skills = skillsService.findAll();
        assertThat(skills).hasSize(24);

        assertThat(skills)
                .extracting(SkillEntity::getSkill)
                .containsExactlyInAnyOrder(
                        Skill.values()
                );
    }
}
