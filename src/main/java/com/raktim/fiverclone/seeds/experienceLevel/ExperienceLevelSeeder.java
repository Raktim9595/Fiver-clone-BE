package com.raktim.fiverclone.seeds.experienceLevel;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExperienceLevelSeeder implements CommandLineRunner {
    private final ExperienceLevelRepo experienceLevelRepo;

    @Override
    public void run(String @NonNull ... args) {
        for (ExperienceLevel level : ExperienceLevel.values()) {
            experienceLevelRepo.findByExperienceLevel(level)
                    .orElseGet(() -> {
                        ExperienceLevelEntity entity = new ExperienceLevelEntity();
                        entity.setExperienceLevel(level);
                        return experienceLevelRepo.save(entity);
                    });
        }
    }
}
