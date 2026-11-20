package com.raktim.fiverclone.role.repo;

import com.raktim.fiverclone.role.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepo extends JpaRepository<RoleEntity, UUID> {
}
