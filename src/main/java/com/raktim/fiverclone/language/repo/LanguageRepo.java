package com.raktim.fiverclone.language.repo;

import com.raktim.fiverclone.language.model.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LanguageRepo extends JpaRepository<LanguageEntity, UUID> {
}
