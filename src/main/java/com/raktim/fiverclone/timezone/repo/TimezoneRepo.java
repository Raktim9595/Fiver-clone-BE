package com.raktim.fiverclone.timezone.repo;

import com.raktim.fiverclone.timezone.model.TimeZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimezoneRepo extends JpaRepository<TimeZoneEntity, UUID> {
}
