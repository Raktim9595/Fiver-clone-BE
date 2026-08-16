package com.raktim.fiverclone.sellerApplication.repo;

import com.raktim.fiverclone.sellerApplication.model.OccupationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OccupationRepo extends JpaRepository<OccupationEntity, UUID> {
}
