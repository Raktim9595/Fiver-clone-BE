package com.raktim.fiverclone.seeds.experienceLevel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExperienceLevelRepo extends JpaRepository<ExperienceLevelEntity, UUID> {
    Optional<ExperienceLevelEntity> findByExperienceLevel(ExperienceLevel level);
}
