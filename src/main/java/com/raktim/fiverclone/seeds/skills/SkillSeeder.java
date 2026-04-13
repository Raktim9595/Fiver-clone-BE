package com.raktim.fiverclone.seeds.skills;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillSeeder implements CommandLineRunner {
    private final SkillRepo skillRepo;

    @Override
    public void run(String @NonNull ... args) {
        for (Skill skill : Skill.values()) {
            skillRepo.findBySkill(skill)
                    .orElseGet(() -> {
                        SkillEntity skillEntity = new SkillEntity();
                        skillEntity.setSkill(skill);
                        return skillRepo.save(skillEntity);
                    });
        }
    }
}