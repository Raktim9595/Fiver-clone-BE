package com.raktim.fiverclone.seeds.skills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkillRepo extends JpaRepository<SkillEntity, UUID> {
    Optional<SkillEntity> findBySkill(Skill skill);
}
