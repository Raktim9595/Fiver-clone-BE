package com.raktim.fiverclone.seeds.skills;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkillsRepo extends JpaRepository<SkillEntity, UUID> {
}
