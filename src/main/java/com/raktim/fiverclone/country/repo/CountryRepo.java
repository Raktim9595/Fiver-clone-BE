package com.raktim.fiverclone.country.repo;

import com.raktim.fiverclone.country.model.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CountryRepo extends JpaRepository<CountryEntity, UUID> {
}
